package org.jeecg.modules.inventory.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.inventory.entity.InventoryOut;

public interface InventoryOutService extends IService<InventoryOut> {

    public String saveToInventoryOut (InventoryOut inventoryOut);
}
