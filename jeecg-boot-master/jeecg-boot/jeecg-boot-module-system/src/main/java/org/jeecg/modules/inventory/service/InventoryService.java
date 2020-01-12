package org.jeecg.modules.inventory.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.inventory.entity.Inventory;
import org.jeecg.modules.inventory.entity.InventoryLog;

public interface InventoryService extends IService<Inventory> {

    public Inventory findInventory(String warehouseId, String mtlId, String unitId);

    public String updateInventory (InventoryLog inventoryLog);
}
