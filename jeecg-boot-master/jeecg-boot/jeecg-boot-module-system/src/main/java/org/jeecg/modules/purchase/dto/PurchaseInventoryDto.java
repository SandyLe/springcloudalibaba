package org.jeecg.modules.purchase.dto;

import org.jeecg.modules.inventory.entity.InventoryIn;
import org.jeecg.modules.inventory.entity.InventoryOut;

/**
 * @Description:
 * @Author: tomkluas
 * @Date: 2020/1/15 17:33
 * @Version: V1.0
 */
public class PurchaseInventoryDto {

    private String msg;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public InventoryIn getInventory() {
        return inventory;
    }

    public void setInventory(InventoryIn inventory) {
        this.inventory = inventory;
    }

    private InventoryIn inventory;

    private InventoryOut inventoryOut;

    public InventoryOut getInventoryOut() {
        return inventoryOut;
    }

    public void setInventoryOut(InventoryOut inventoryOut) {
        this.inventoryOut = inventoryOut;
    }
}
