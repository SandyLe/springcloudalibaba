package org.jeecg.modules.inventory.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.jeecg.common.exception.JeecgBootException;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.basic.entity.Material;
import org.jeecg.modules.basic.enums.InventoryOperation;
import org.jeecg.modules.basic.service.MaterialService;
import org.jeecg.modules.inventory.entity.Inventory;
import org.jeecg.modules.inventory.entity.InventoryLog;
import org.jeecg.modules.inventory.mapper.InventoryMapper;
import org.jeecg.modules.inventory.service.InventoryLogService;
import org.jeecg.modules.inventory.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Service
public class InventoryServiceImpl extends ServiceImpl<InventoryMapper, Inventory>  implements InventoryService {

    @Autowired
    private InventoryMapper inventoryMapper;

    @Autowired
    private MaterialService materialService;
    @Autowired
    private InventoryLogService inventoryLogService;

    @Override
    public Inventory findInventory(String warehouseId, String mtlId, String unitId) {

        Inventory inventory = new Inventory();
        inventory.setWarehouseId(warehouseId);
        inventory.setMtlId(mtlId);
        inventory.setUnitId(unitId);
        Map<String, String[]> params = new HashMap<>();
        params.put("mtlId", new String[]{mtlId});
        params.put("warehouseId", new String[]{warehouseId});
        if (StringUtils.isNotBlank(unitId)) {
            params.put("unitId", new String[]{unitId});
        }
        QueryWrapper<Inventory> queryWrapper = QueryGenerator.initQueryWrapper(inventory, params);
        return inventoryMapper.selectOne(queryWrapper);
    }

    @Override
    @Transactional
    public String updateInventory(InventoryLog inventoryLog) {
        Inventory inventory = findInventory(inventoryLog.getWarehouseId(), inventoryLog.getMtlId(), inventoryLog.getUnitId());
        if (inventoryLog.getOperationId() == InventoryOperation.STOCKING.getId() || inventoryLog.getOperationId() == InventoryOperation.PURCHASEIN.getId()) {
            if( null == inventory ) {
                inventory = new Inventory();
                inventory.setMtlId(inventoryLog.getMtlId());
                inventory.setWarehouseId(inventoryLog.getWarehouseId());
                inventory.setUnitId(inventoryLog.getUnitId());
                inventory.setStockAmount(BigDecimal.ZERO);
            }
            inventoryLog.setBeforeAmount(inventory.getStockAmount());
            if (inventoryLog.getOperationId() == InventoryOperation.STOCKING.getId()) {
                if (null != inventory && inventory.getStockAmount().compareTo(inventoryLog.getBeforeAmount()) !=0 ) {
                    throw new JeecgBootException("库存有变化，请确认后编辑盘点单再操作！");
                }
                inventory.setStockAmount(inventoryLog.getStockAmount());
            } else if (inventoryLog.getOperationId() == InventoryOperation.PURCHASEIN.getId()){
                inventory.setStockAmount(inventory.getStockAmount().add(inventoryLog.getOptAmount()));
                inventoryLog.setStockAmount(inventory.getStockAmount());
            }

        } else if( inventoryLog.getOperationId() == InventoryOperation.SALEOUT.getId() )  {
            if (null == inventory || inventory.getStockAmount().compareTo(inventoryLog.getOptAmount()) < 0) {
                Material material = materialService.getById(inventoryLog.getMtlId());
                throw new JeecgBootException("仓库中产品："+material.getName() + " 库存不足！");
            }
            inventoryLog.setBeforeAmount(inventory.getStockAmount());
            inventory.setStockAmount(inventory.getStockAmount().subtract(inventoryLog.getOptAmount()));
            inventoryLog.setStockAmount(inventory.getStockAmount());
        }
        saveOrUpdate(inventory);
        inventoryLogService.saveOrUpdate(inventoryLog);
        return inventoryLog.getId();
    }
}
