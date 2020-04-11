package org.jeecg.modules.inventory.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.jeecg.common.enums.BillStatus;
import org.jeecg.common.enums.BillType;
import org.jeecg.common.enums.InventoryOperation;
import org.jeecg.common.enums.RowSts;
import org.jeecg.common.util.Constants;
import org.jeecg.modules.basic.service.BillCodeBuilderService;
import org.jeecg.modules.combind.entity.Assemble;
import org.jeecg.modules.combind.entity.Teardown;
import org.jeecg.modules.combind.service.AssembleDtlService;
import org.jeecg.modules.combind.service.AssembleService;
import org.jeecg.modules.combind.service.TeardownDtlService;
import org.jeecg.modules.combind.service.TeardownService;
import org.jeecg.modules.inventory.entity.*;

import org.jeecg.modules.inventory.mapper.InventoryOutMapper;
import org.jeecg.modules.inventory.service.*;
import org.jeecg.modules.purchase.entity.PurchaseReturn;
import org.jeecg.modules.purchase.entity.PurchaseReturnMtl;
import org.jeecg.modules.purchase.service.PurchaseReturnMtlService;
import org.jeecg.modules.inventory.dto.PreInventoryOutMtl;
import org.jeecg.modules.purchase.service.PurchaseReturnService;
import org.jeecg.modules.saleorder.entity.SaleOrder;
import org.jeecg.modules.saleorder.entity.SaleOrderMtl;
import org.jeecg.modules.saleorder.service.SaleOrderMtlService;
import org.jeecg.modules.saleorder.service.SaleOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class InventoryOutServiceImpl extends ServiceImpl<InventoryOutMapper, InventoryOut>  implements InventoryOutService {

//    @Autowired
//    private InventoryOutMapper inventoryOutMapper;

    @Autowired
    private SaleOrderMtlService saleOrderMtlService;
    @Autowired
    private PurchaseReturnMtlService iPurchaseReturnMtlService;
    @Autowired
    private InventoryOutMtlService inventoryOutMtlService;
    @Autowired
    private SaleOrderService saleOrderService;
    @Autowired
    private InventoryService inventoryService;
    @Autowired
    private InventoryLogService inventoryLogService;
    @Autowired
    private PurchaseReturnService purchaseReturnService;
    @Autowired
    private AllotDtlService allotDtlService;
    @Autowired
    private AllotService allotService;
    @Autowired
    private AssembleService assembleService;
    @Autowired
    private AssembleDtlService assembleDtlService;
    @Autowired
    private TeardownService teardownService;
    @Autowired
    private TeardownDtlService teardownDtlService;
    @Autowired
    private BillCodeBuilderService billCodeBuilderService;

    @Override
    @Transactional
    public Boolean stockOut(List<PreInventoryOutMtl> mtls) {

        InventoryOut info = getById(mtls.get(0).getBillId());
        if (null != info) {
            for (PreInventoryOutMtl mtl : mtls) {
                InventoryLog inventoryLog = new InventoryLog(mtl.getSourceId(), info.getSourceBillType(), mtl.getMtlId(),
                        info.getWarehouseId(), null, mtl.getQuantity(), null, mtl.getUnitId(), mtl.getOperationId(), null);
                inventoryService.updateInventory(inventoryLog);
            }
            List<PreInventoryOutMtl> deliveryMtls = getDeliveryMtlList(info.getId(), info.getSourceId());
            if (mtls.get(0).getSourceBillType() == BillType.SALEORDER.getId()) {
                SaleOrder saleOrder = saleOrderService.getById(info.getSourceId());
                if (CollectionUtils.isNotEmpty(deliveryMtls)) {
                    saleOrder.setBillStatus(BillStatus.PARTICIALSTOCK.getId());
                    info.setBillStatus(BillStatus.PARTICIALSTOCK.getId());
                } else {
                    saleOrder.setBillStatus(BillStatus.STOCKED.getId());
                    info.setBillStatus(BillStatus.STOCKED.getId());
                }
                saleOrderService.updateById(saleOrder);
            } else if (mtls.get(0).getSourceBillType() == BillType.PURCHASERETURNORDER.getId()) {
                PurchaseReturn purchaseReturn = purchaseReturnService.getById(info.getSourceId());
                if (CollectionUtils.isNotEmpty(deliveryMtls)) {
                    purchaseReturn.setBillStatus(BillStatus.PARTICIALSTOCK.getId());
                    info.setBillStatus(BillStatus.PARTICIALSTOCK.getId());
                } else {
                    purchaseReturn.setBillStatus(BillStatus.STOCKED.getId());
                    info.setBillStatus(BillStatus.STOCKED.getId());
                }
                purchaseReturnService.updateById(purchaseReturn);
            } else if (mtls.get(0).getSourceBillType() == BillType.ALLOT.getId()) {
                Allot allot = allotService.getById(info.getSourceId());
                if (CollectionUtils.isNotEmpty(deliveryMtls)) {
                    allot.setBillStatus(BillStatus.PARTICIALSTOCK.getId());
                    info.setBillStatus(BillStatus.PARTICIALSTOCK.getId());
                } else {
                    allot.setBillStatus(BillStatus.STOCKED.getId());
                    info.setBillStatus(BillStatus.STOCKED.getId());
                }
                allotService.updateById(allot);
            } else if (mtls.get(0).getSourceBillType() == BillType.ASSEMBLE.getId()) {
                Assemble assemble = assembleService.getById(info.getSourceId());
                if (CollectionUtils.isNotEmpty(deliveryMtls)) {
                    assemble.setBillStatus(BillStatus.PARTICIALSTOCK.getId());
                    info.setBillStatus(BillStatus.PARTICIALSTOCK.getId());
                } else {
                    assemble.setBillStatus(BillStatus.STOCKED.getId());
                    info.setBillStatus(BillStatus.STOCKED.getId());
                }
                assembleService.updateById(assemble);
            } else if (mtls.get(0).getSourceBillType() == BillType.TEARDOWN.getId()) {
                Teardown teardown = teardownService.getById(info.getSourceId());
                if (CollectionUtils.isNotEmpty(deliveryMtls)) {
                    teardown.setBillStatus(BillStatus.PARTICIALSTOCK.getId());
                    info.setBillStatus(BillStatus.PARTICIALSTOCK.getId());
                } else {
                    teardown.setBillStatus(BillStatus.STOCKED.getId());
                    info.setBillStatus(BillStatus.STOCKED.getId());
                }
                teardownService.updateById(teardown);
            }
            updateById(info);
        }
        return true;
    }
    @Override
    public String saveToInventoryOut (InventoryOut inventoryOut) {
        if (StringUtils.isEmpty(inventoryOut.getId())) {
            inventoryOut.setCode(billCodeBuilderService.getBillCode(BillType.STOREOUT.getId()));
        }
        // 保存主表
        save(inventoryOut);

        List<InventoryOutMtl> inventoryOutMtls = Lists.newArrayList();
        if (inventoryOut.getSourceBillType() == BillType.SALEORDER.getId()) {
            LambdaQueryWrapper<SaleOrderMtl> queryWrapper = new LambdaQueryWrapper<SaleOrderMtl>().eq(SaleOrderMtl::getSourceId, inventoryOut.getSourceId());
            List<SaleOrderMtl> saleOrderMtls = saleOrderMtlService.list(queryWrapper);
            if (CollectionUtils.isNotEmpty(saleOrderMtls)) {
                saleOrderMtls.forEach(o ->{
                    inventoryOutMtls.add(new InventoryOutMtl(inventoryOut.getId(), o.getSourceId(), inventoryOut.getSourceBillType(), o.getMtlId(), o.getQuantity(), o.getUnitId(), RowSts.EFFECTIVE.getId()));
                });
            }
        } else if (inventoryOut.getSourceBillType() == BillType.PURCHASERETURNORDER.getId()) {
            LambdaQueryWrapper<PurchaseReturnMtl> queryWrapper = new LambdaQueryWrapper<PurchaseReturnMtl>().eq(PurchaseReturnMtl::getSourceId, inventoryOut.getSourceId());
            List<PurchaseReturnMtl> purchaseReturnMtls = iPurchaseReturnMtlService.list(queryWrapper);
            if (CollectionUtils.isNotEmpty(purchaseReturnMtls)) {
                purchaseReturnMtls.forEach(o ->{
                    inventoryOutMtls.add(new InventoryOutMtl(inventoryOut.getId(), o.getSourceId(), inventoryOut.getSourceBillType(), o.getMtlId(), o.getQuantity(), o.getUnitId(), RowSts.EFFECTIVE.getId()));
                });
            }
        } else if (inventoryOut.getSourceBillType() == BillType.ALLOT.getId()) {
            LambdaQueryWrapper<AllotDtl> queryWrapper = new LambdaQueryWrapper<AllotDtl>().eq(AllotDtl::getSourceId, inventoryOut.getSourceId());
            List<AllotDtl> allotDtls = allotDtlService.list(queryWrapper);
            if (CollectionUtils.isNotEmpty(allotDtls)) {
                allotDtls.forEach(o ->{
                    inventoryOutMtls.add(new InventoryOutMtl(inventoryOut.getId(), o.getSourceId(), inventoryOut.getSourceBillType(), o.getMtlId(), o.getAllotAmount(), o.getUnitId(), RowSts.EFFECTIVE.getId()));
                });
            }
        } else if (inventoryOut.getSourceBillType() == BillType.ASSEMBLE.getId()) {
            Assemble assemble = assembleService.getById(inventoryOut.getSourceId());
            inventoryOutMtls.add(new InventoryOutMtl(inventoryOut.getId(), assemble.getId(), inventoryOut.getSourceBillType(), assemble.getMtlId(), o.getAllotAmount(), o.getUnitId(), RowSts.EFFECTIVE.getId()));
        } else if (inventoryOut.getSourceBillType() == BillType.ALLOT.getId()) {
            LambdaQueryWrapper<AllotDtl> queryWrapper = new LambdaQueryWrapper<AllotDtl>().eq(AllotDtl::getSourceId, inventoryOut.getSourceId());
            List<AllotDtl> allotDtls = allotDtlService.list(queryWrapper);
            if (CollectionUtils.isNotEmpty(allotDtls)) {
                allotDtls.forEach(o ->{
                    inventoryOutMtls.add(new InventoryOutMtl(inventoryOut.getId(), o.getSourceId(), inventoryOut.getSourceBillType(), o.getMtlId(), o.getAllotAmount(), o.getUnitId(), RowSts.EFFECTIVE.getId()));
                });
            }
        }
        inventoryOutMtlService.saveBatch(inventoryOutMtls);
        return inventoryOut.getId();
    }

    @Override
    public List<PreInventoryOutMtl> getDeliveryMtlList(String id, String sourceId) {
        List<PreInventoryOutMtl> preInventoryOutMtls = Lists.newArrayList();
        InventoryOut inventoryOut = getById(id);
        Integer operationId = Constants.OUTOPERATIONS.get(inventoryOut.getSourceBillType());
        LambdaQueryWrapper<InventoryOutMtl> querySaleMtlWrapper = new QueryWrapper<InventoryOutMtl>().lambda().eq(InventoryOutMtl::getSourceBillId, sourceId).eq(InventoryOutMtl::getRowSts, RowSts.EFFECTIVE.getId());
        LambdaQueryWrapper<InventoryLog> queryInventoryLogWrapper = new QueryWrapper<InventoryLog>().lambda().eq(InventoryLog::getSourceId, sourceId);
        queryInventoryLogWrapper.eq(InventoryLog::getOperationId, operationId);
        List<InventoryOutMtl> inventoryOutMtls = inventoryOutMtlService.list(querySaleMtlWrapper);
        List<InventoryLog> inventoryLogs = inventoryLogService.list(queryInventoryLogWrapper);
        Map<String, BigDecimal> mtlDeliveryQtyMap = new HashMap<>();
        inventoryLogs.stream().forEach(o->{
            mtlDeliveryQtyMap.put(o.getMtlId(), null == mtlDeliveryQtyMap.get(o.getMtlId()) ? o.getOptAmount() : mtlDeliveryQtyMap.get(o.getMtlId()).add(o.getOptAmount()));
        });
        inventoryOutMtls.forEach(o->{
            BigDecimal tempAmout = o.getQuantity();
            if (null != mtlDeliveryQtyMap.get(o.getMtlId())) {
                tempAmout = tempAmout.subtract(mtlDeliveryQtyMap.get(o.getMtlId()));
            }
            if (tempAmout.compareTo(BigDecimal.ZERO) > 0 ) {
                PreInventoryOutMtl preInventoryOutMtl = new PreInventoryOutMtl();
                preInventoryOutMtl.setBillId(id);
                preInventoryOutMtl.setSourceId(sourceId);
                preInventoryOutMtl.setMtlId(o.getMtlId());
                preInventoryOutMtl.setSourceBillType(o.getSourceBillType());
                preInventoryOutMtl.setQuantity(tempAmout);
                preInventoryOutMtl.setUnitId(o.getUnitId());
                preInventoryOutMtl.setOperationId(operationId);
                preInventoryOutMtls.add(preInventoryOutMtl);
            }
        });
        return preInventoryOutMtls;
    }

    @Override
    public InventoryOut queryBySourceId(String sourceId) {

        return this.baseMapper.queryBySourceId(sourceId);
    }

    @Override
    public void deleteBySourceId(String sourceId) {

        InventoryOut exist = getOne(new LambdaQueryWrapper<InventoryOut>().eq(InventoryOut::getSourceId, sourceId).eq(InventoryOut::getRowSts, RowSts.EFFECTIVE.getId()));
        if (null != exist) {
            exist.setRowSts(RowSts.DELETED.getId());
            updateById(exist);

            List<InventoryOutMtl> inventoryInMtls = inventoryOutMtlService.list(new LambdaQueryWrapper<InventoryOutMtl>().eq(InventoryOutMtl::getSourceId, exist.getId()));
            if (CollectionUtils.isNotEmpty(inventoryInMtls)) {
                inventoryInMtls.stream().forEach(o->{
                    o.setRowSts(RowSts.DELETED.getId());
                });
                inventoryOutMtlService.updateBatchById(inventoryInMtls);
            }
        }
    }
}
