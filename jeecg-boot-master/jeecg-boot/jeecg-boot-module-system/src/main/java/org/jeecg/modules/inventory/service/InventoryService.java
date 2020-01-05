package org.jeecg.modules.inventory.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.inventory.entity.Inventory;

public interface InventoryService extends IService<Inventory> {

    public Inventory findInventory(String warehouseId, String mtlId, String unitId);
}
