package org.jeecg.modules.inventory.dto;

import io.swagger.annotations.ApiModel;
import org.jeecg.modules.inventory.entity.Allot;
import org.jeecg.modules.inventory.entity.AllotDtl;

import java.util.List;

@ApiModel(value = "AllotDto", description = "调拨单")
public class AllotDto extends Allot {

    private List<AllotDtl> detaillist;

    public List<AllotDtl> getDetaillist() {
        return detaillist;
    }

    public void setDetaillist(List<AllotDtl> detaillist) {
        this.detaillist = detaillist;
    }
}
