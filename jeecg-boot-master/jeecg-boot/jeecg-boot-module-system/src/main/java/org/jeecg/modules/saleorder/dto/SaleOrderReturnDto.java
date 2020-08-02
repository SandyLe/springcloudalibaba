package org.jeecg.modules.saleorder.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.jeecg.modules.saleorder.entity.SaleOrderReturn;
import org.jeecg.modules.saleorder.entity.SaleOrderReturnMtl;

import java.util.List;

@ApiModel("销售退货DTO")
public class SaleOrderReturnDto extends SaleOrderReturn {

    @ApiModelProperty("产品")
    private List<SaleOrderReturnMtl> detaillist;

    public List<SaleOrderReturnMtl> getDetaillist() {
        return detaillist;
    }

    public void setDetaillist(List<SaleOrderReturnMtl> detaillist) {
        this.detaillist = detaillist;
    }
}
