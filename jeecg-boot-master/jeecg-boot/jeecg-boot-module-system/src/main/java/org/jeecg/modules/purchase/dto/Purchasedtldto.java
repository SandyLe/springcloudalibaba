package org.jeecg.modules.purchase.dto;

import org.jeecg.modules.purchase.entity.Purchase;
import org.jeecg.modules.purchase.entity.Purchasedtl;

import java.util.List;

public class Purchasedtldto extends Purchase {
    private List<Purchasedtl> detaillist;

    public List<Purchasedtl> getDetaillist() {
        return detaillist;
    }

    public void setDetaillist(List<Purchasedtl> detaillist) {
        this.detaillist = detaillist;
    }
}
