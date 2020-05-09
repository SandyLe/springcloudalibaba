package org.jeecg.modules.changeorder.dto;

import io.swagger.annotations.ApiModel;
import org.jeecg.modules.changeorder.entity.ChangeOrder;
import org.jeecg.modules.changeorder.entity.ChangeOrderDtl;

import java.util.List;

@ApiModel(value = "ChangeOrderDto", description = "维修单DTO")
public class ChangeOrderDto extends ChangeOrder {

    private List<ChangeOrderDtl> detaillist;

    public List<ChangeOrderDtl> getDetaillist() {
        return detaillist;
    }

    public void setDetaillist(List<ChangeOrderDtl> detaillist) {
        this.detaillist = detaillist;
    }
}
