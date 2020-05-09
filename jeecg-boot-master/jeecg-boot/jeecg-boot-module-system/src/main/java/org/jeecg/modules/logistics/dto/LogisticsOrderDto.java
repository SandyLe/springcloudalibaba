package org.jeecg.modules.logistics.dto;

import io.swagger.annotations.ApiModel;
import org.jeecg.modules.logistics.entity.LogisticsOrder;
import org.jeecg.modules.logistics.entity.LogisticsOrderDtl;

import java.util.List;

@ApiModel(value = "LogisticsOrderDto", description = "物流订单DTO")
public class LogisticsOrderDto extends LogisticsOrder {

    private List<LogisticsOrderDtl> detaillist;

    public List<LogisticsOrderDtl> getDetaillist() {
        return detaillist;
    }

    public void setDetaillist(List<LogisticsOrderDtl> detaillist) {
        this.detaillist = detaillist;
    }
}
