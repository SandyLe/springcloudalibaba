package org.jeecg.modules.inventory.dto;

import io.swagger.annotations.ApiModel;
import org.jeecg.modules.inventory.entity.Allot;
import org.jeecg.modules.inventory.entity.AllotDtl;

import java.util.List;

@ApiModel(value = "AllotDto", description = "调拨单")
public class AllotDto extends Allot {

    private List<AllotDtl> dtlList;

    public List<AllotDtl> getDtlList() {
        return dtlList;
    }

    public void setDtlList(List<AllotDtl> dtlList) {
        this.dtlList = dtlList;
    }
}
