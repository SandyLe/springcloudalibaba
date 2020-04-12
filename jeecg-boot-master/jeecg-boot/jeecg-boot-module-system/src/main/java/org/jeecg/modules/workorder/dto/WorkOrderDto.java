package org.jeecg.modules.workorder.dto;

import io.swagger.annotations.ApiModel;
import org.jeecg.modules.workorder.entity.WorkOrderDtl;
import org.jeecg.modules.workorder.entity.WorkOrder;

import java.util.List;

@ApiModel(value = "WorkOrderDto", description = "工单DTO")
public class WorkOrderDto extends WorkOrder {

    private List<WorkOrderDtl> detaillist;

    public List<WorkOrderDtl> getDetaillist() {
        return detaillist;
    }

    public void setDetaillist(List<WorkOrderDtl> detaillist) {
        this.detaillist = detaillist;
    }
}
