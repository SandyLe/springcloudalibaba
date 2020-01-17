package org.jeecg.modules.purchase.dto;

import org.jeecg.modules.inventory.entity.InventoryOut;
import org.jeecg.modules.purchase.entity.Purchase;
import org.jeecg.modules.purchase.entity.PurchaseDtl;

import java.util.List;

public class PurchaseDtlDto extends Purchase {
    private List<PurchaseDtl> detaillist;

    public List<PurchaseDtl> getDetaillist() {
        return detaillist;
    }

    public void setDetaillist(List<PurchaseDtl> detaillist) {
        this.detaillist = detaillist;
    }

    private InventoryOut inventoryOut;
    public InventoryOut getInventoryOut() {
        return inventoryOut;
    }

    public void setInventoryOut(InventoryOut inventoryOut) {
        this.inventoryOut = inventoryOut;
    }

}
