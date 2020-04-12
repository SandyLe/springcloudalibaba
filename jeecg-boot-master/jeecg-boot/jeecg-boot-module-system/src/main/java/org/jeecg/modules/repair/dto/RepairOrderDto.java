package org.jeecg.modules.repair.dto;

import io.swagger.annotations.ApiModel;
import org.jeecg.modules.repair.entity.RepairOrder;
import org.jeecg.modules.repair.entity.RepairOrderDtl;

import java.util.List;

@ApiModel(value = "RepairOrderDto", description = "维修单DTO")
public class RepairOrderDto extends RepairOrder {

    private List<RepairOrderDtl> detaillist;

    public List<RepairOrderDtl> getDetaillist() {
        return detaillist;
    }

    public void setDetaillist(List<RepairOrderDtl> detaillist) {
        this.detaillist = detaillist;
    }
}
