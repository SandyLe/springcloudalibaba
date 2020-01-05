package org.jeecg.modules.stocking.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.inventory.entity.Inventory;
import org.jeecg.modules.inventory.service.InventoryService;
import org.jeecg.modules.stocking.entity.Stocking;
import org.jeecg.modules.stocking.mapper.StockingMapper;
import org.jeecg.modules.stocking.service.StockingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StockingServiceImpl extends ServiceImpl<StockingMapper, Stocking>  implements StockingService {

    @Autowired
    private StockingMapper stockingMapper;

    @Autowired
    private InventoryService inventoryService;

    @Transactional
    @Override
    public String handleStocking(String id) {
        String msg = null;
        Stocking stocking = getById(id);
        Inventory inventory = inventoryService.findInventory(stocking.getWarehouseId(), stocking.getMtlId(), stocking.getUnitId());
        if (null != inventory && inventory.getStockAmount().compareTo(stocking.getBeforeAmount()) !=0 ) {
            msg = "库存有变化，请确认后编辑盘点单再操作！";
        } else {
            inventory = null == inventory ? new Inventory() : inventory;
            inventory.setWarehouseId(stocking.getWarehouseId());
            inventory.setUnitId(stocking.getUnitId());
            inventory.setMtlId(stocking.getMtlId());
            inventory.setStockAmount(stocking.getStockAmount());
            inventoryService.saveOrUpdate(inventory);
        }
        return msg;
    }
}
