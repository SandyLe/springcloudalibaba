package org.jeecg.modules.inventory.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import org.apache.commons.lang.StringUtils;
import org.jeecg.common.exception.JeecgBootException;
import org.jeecg.modules.basic.entity.Material;
import org.jeecg.common.enums.InventoryOperation;
import org.jeecg.modules.basic.service.MaterialService;
import org.jeecg.modules.inventory.entity.Inventory;
import org.jeecg.modules.inventory.entity.InventoryDtl;
import org.jeecg.modules.inventory.entity.InventoryLog;
import org.jeecg.modules.inventory.entity.InventoryOptDtl;
import org.jeecg.modules.inventory.mapper.InventoryMapper;
import org.jeecg.modules.inventory.service.InventoryDtlService;
import org.jeecg.modules.inventory.service.InventoryLogService;
import org.jeecg.modules.inventory.service.InventoryOptDtlService;
import org.jeecg.modules.inventory.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class InventoryServiceImpl extends ServiceImpl<InventoryMapper, Inventory>  implements InventoryService {

    @Autowired
    private InventoryMapper inventoryMapper;
    @Autowired
    private MaterialService materialService;
    @Autowired
    private InventoryLogService inventoryLogService;
    @Autowired
    private InventoryDtlService inventoryDtlService;
    @Autowired
    private InventoryOptDtlService inventoryOptDtlService;

    @Override
    public Inventory findInventory(String warehouseId, String mtlId, String unitId) {

        LambdaQueryWrapper<Inventory> lambdaQueryWrapper = new LambdaQueryWrapper<Inventory>();
        lambdaQueryWrapper.eq(Inventory::getMtlId, mtlId);
        lambdaQueryWrapper.eq(Inventory::getWarehouseId, warehouseId);
        if (StringUtils.isNotBlank(unitId)) {
            lambdaQueryWrapper.eq(Inventory::getUnitId, unitId);
        }
        return inventoryMapper.selectOne(lambdaQueryWrapper);
    }

    @Override
    @Transactional
    public String updateInventory(InventoryLog inventoryLog) {
        Inventory inventory = findInventory(inventoryLog.getWarehouseId(), inventoryLog.getMtlId(), inventoryLog.getUnitId());
        if (inventoryLog.getOperationId() == InventoryOperation.STOCKING.getId()
                || inventoryLog.getOperationId() == InventoryOperation.PURCHASEIN.getId()
                || inventoryLog.getOperationId() == InventoryOperation.SALERETURNIN.getId()
                || inventoryLog.getOperationId() == InventoryOperation.TEARDOWNIN.getId()
                || inventoryLog.getOperationId() == InventoryOperation.ASSEMBLEIN.getId()
                || inventoryLog.getOperationId() == InventoryOperation.ALLOTIN.getId()
                || inventoryLog.getOperationId() == InventoryOperation.CHANGEIN.getId()) {

            List<InventoryOptDtl> optDtls = Lists.newArrayList();
            if( null == inventory ) {
                inventory = new Inventory();
                inventory.setMtlId(inventoryLog.getMtlId());
                inventory.setWarehouseId(inventoryLog.getWarehouseId());
                inventory.setUnitId(inventoryLog.getUnitId());
                inventory.setStockAmount(BigDecimal.ZERO);
            }
            InventoryDtl inventoryDtl = null;
            InventoryOptDtl inventoryOptDtl = new InventoryOptDtl();
            inventoryOptDtl.setSourceId(inventoryLog.getSourceId());
            inventoryOptDtl.setSourceBillType(inventoryLog.getSourceBillType());
            if (StringUtils.isNotBlank(inventoryLog.getBatchNo())) {
                inventoryDtl = inventoryDtlService.findInventoryDtl(inventoryLog.getWarehouseId(), inventoryLog.getBatchNo(), inventory.getMtlId(), inventory.getUnitId());
            }
            if (inventoryLog.getOperationId() == InventoryOperation.STOCKING.getId()) {
                if (null != inventory && inventory.getStockAmount().compareTo(inventoryLog.getBeforeAmount()) !=0 ) {
                    throw new JeecgBootException("库存有变化，请确认后编辑盘点单再操作！");
                }
                if (inventoryLog.getStockAmount().compareTo(inventoryLog.getBeforeAmount()) > 0) {
                    if (null == inventoryDtl) { // 新增明细 没有批次号
                        inventoryDtl = new InventoryDtl();
                        inventoryDtl.setMtlId(inventoryLog.getMtlId());
                        inventoryDtl.setBatchNo(inventoryLog.getBatchNo());
                        inventoryDtl.setUnitId(inventoryLog.getUnitId());
                        inventoryDtl.setWarehouseId(inventoryLog.getWarehouseId());
                        inventoryDtl.setStockAmount(inventoryLog.getStockAmount().subtract(inventoryLog.getBeforeAmount()));
                        inventoryDtl.setAvailableAmount(inventoryLog.getStockAmount().subtract(inventoryLog.getBeforeAmount()));
                        inventoryOptDtl.setOperationId(InventoryOperation.STOCKINGADD.getId());
                        inventoryOptDtl.setOptAmount(inventoryLog.getStockAmount().subtract(inventoryLog.getBeforeAmount()));
                    } else {
                        inventoryDtl.setAvailableAmount(inventoryLog.getStockAmount());
                        inventoryOptDtl.setOperationId(InventoryOperation.STOCKING.getId());
                        inventoryOptDtl.setOptAmount(inventoryLog.getStockAmount());
                    }
                } else if (inventoryLog.getStockAmount().compareTo(inventoryLog.getBeforeAmount()) < 0) {
                    // 扣除
                    if (null != inventoryDtl) { //有批次号
                        inventoryDtl.setAvailableAmount(inventoryLog.getStockAmount());
                        inventoryOptDtl.setOperationId(InventoryOperation.STOCKING.getId());
                        inventoryOptDtl.setOptAmount(inventoryLog.getStockAmount());
                    } else {
                        BigDecimal change = inventoryLog.getBeforeAmount().subtract(inventoryLog.getStockAmount());
                        List<InventoryDtl> inventoryDtls = inventoryDtlService.findAvailableIntoryDtlList(inventoryLog.getWarehouseId(), inventoryLog.getMtlId(), inventoryLog.getUnitId());
                        for (InventoryDtl dtl : inventoryDtls) {
                            inventoryOptDtl = new InventoryOptDtl();
                            inventoryOptDtl.setSourceId(inventoryLog.getSourceId());
                            inventoryOptDtl.setSourceBillType(inventoryLog.getSourceBillType());
                            inventoryOptDtl.setOperationId(InventoryOperation.STOCKINGSUB.getId());
                            inventoryOptDtl.setDtlId(dtl.getId());
                            if (change.compareTo(dtl.getAvailableAmount()) >= 0) {
                                dtl.setAvailableAmount(BigDecimal.ZERO);
                                change = change.subtract(dtl.getAvailableAmount());
                                inventoryDtlService.saveOrUpdate(dtl);
                                inventoryOptDtl.setOptAmount(dtl.getAvailableAmount());
                                inventoryOptDtlService.saveOrUpdate(inventoryOptDtl);
                            } else {
                                dtl.setAvailableAmount(dtl.getAvailableAmount().subtract(change));
                                inventoryOptDtl.setOptAmount(change);
                                change = BigDecimal.ZERO;
                                inventoryDtlService.saveOrUpdate(dtl);
                                inventoryOptDtlService.saveOrUpdate(inventoryOptDtl);
                                break;
                            }
                        }
                    }
                } else {
                    return "";
                }
                inventory.setStockAmount(inventoryLog.getStockAmount());
            } else {
                if (null == inventoryDtl) {
                    inventoryDtl = new InventoryDtl();
                    inventoryDtl.setWarehouseId(inventoryLog.getWarehouseId());
                    inventoryDtl.setMtlId(inventoryLog.getMtlId());
                    inventoryDtl.setBatchNo(inventoryLog.getBatchNo());
                    inventoryDtl.setUnitId(inventoryLog.getUnitId());
                    inventoryDtl.setStockAmount(BigDecimal.ZERO);
                    inventoryDtl.setAvailableAmount(BigDecimal.ZERO);
                }
                inventoryLog.setBeforeAmount(inventory.getStockAmount());
                inventoryDtl.setAvailableAmount(inventoryDtl.getAvailableAmount().add(inventoryLog.getOptAmount()));
                inventoryDtl.setStockAmount(inventoryDtl.getStockAmount().add(inventoryLog.getOptAmount()));
                inventory.setStockAmount(inventory.getStockAmount().add(inventoryLog.getOptAmount()));
                inventoryLog.setStockAmount(inventory.getStockAmount());
                inventoryOptDtl.setOptAmount(inventoryLog.getOptAmount());
                inventoryOptDtl.setOperationId(inventoryLog.getOperationId());
            }
            if (null != inventoryDtl ){
                saveOrUpdate(inventory);
                inventoryDtl.setSourceId(inventory.getId());
                inventoryDtlService.saveOrUpdate(inventoryDtl);
                inventoryOptDtl.setDtlId(inventoryDtl.getId());
                inventoryOptDtlService.saveOrUpdate(inventoryOptDtl);
            }
        } else if( inventoryLog.getOperationId() == InventoryOperation.SALEOUT.getId()
                || inventoryLog.getOperationId() == InventoryOperation.PURCHASERETURNOUT.getId()
                || inventoryLog.getOperationId() == InventoryOperation.TEARDOWNOUT.getId()
                || inventoryLog.getOperationId() == InventoryOperation.ASSEMBLEOUT.getId()
                || inventoryLog.getOperationId() == InventoryOperation.ALLOTOUT.getId()
                || inventoryLog.getOperationId() == InventoryOperation.CHANGEOUT.getId()) {

            if (null == inventory || inventory.getStockAmount().compareTo(inventoryLog.getOptAmount()) < 0) {
                Material material = materialService.getById(inventoryLog.getMtlId());
                throw new JeecgBootException("仓库中产品："+material.getName() + " 库存不足！");
            }

            BigDecimal change = inventoryLog.getOptAmount();
            List<InventoryDtl> inventoryDtls = inventoryDtlService.findAvailableIntoryDtlList(inventoryLog.getWarehouseId(), inventoryLog.getMtlId(), inventoryLog.getUnitId());
            for (InventoryDtl dtl : inventoryDtls) {
                InventoryOptDtl inventoryOptDtl = new InventoryOptDtl();
                inventoryOptDtl.setSourceId(inventoryLog.getSourceId());
                inventoryOptDtl.setSourceBillType(inventoryLog.getSourceBillType());
                inventoryOptDtl.setOperationId(InventoryOperation.STOCKINGSUB.getId());
                inventoryOptDtl.setDtlId(dtl.getId());
                if (change.compareTo(dtl.getAvailableAmount()) >= 0) {
                    dtl.setAvailableAmount(BigDecimal.ZERO);
                    change = change.subtract(dtl.getAvailableAmount());
                    inventoryDtlService.saveOrUpdate(dtl);
                    inventoryOptDtl.setOptAmount(dtl.getAvailableAmount());
                    inventoryOptDtlService.saveOrUpdate(inventoryOptDtl);
                } else {
                    dtl.setAvailableAmount(dtl.getAvailableAmount().subtract(change));
                    inventoryOptDtl.setOptAmount(change);
                    change = BigDecimal.ZERO;
                    inventoryDtlService.saveOrUpdate(dtl);
                    inventoryOptDtlService.saveOrUpdate(inventoryOptDtl);
                    break;
                }
            }
            inventoryLog.setBeforeAmount(inventory.getStockAmount());
            inventory.setStockAmount(inventory.getStockAmount().subtract(inventoryLog.getOptAmount()));
            inventoryLog.setStockAmount(inventory.getStockAmount());
            saveOrUpdate(inventory);
        }
        inventoryLogService.saveOrUpdate(inventoryLog);
        return inventoryLog.getId();
    }
}
