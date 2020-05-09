package org.jeecg.modules.stocking.dto;

import io.swagger.annotations.ApiModel;
import org.jeecg.modules.stocking.entity.Stocking;
import org.jeecg.modules.stocking.entity.StockingDtl;

import java.util.List;

@ApiModel(value = "StockingDto", description = "盘点单")
public class StockingDto extends Stocking {

    private List<StockingDtl> dtlList;

    public List<StockingDtl> getDtlList() {
        return dtlList;
    }

    public void setDtlList(List<StockingDtl> dtlList) {
        this.dtlList = dtlList;
    }
}
