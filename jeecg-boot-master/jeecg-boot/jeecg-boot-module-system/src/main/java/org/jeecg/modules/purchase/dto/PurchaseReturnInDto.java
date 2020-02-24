package org.jeecg.modules.purchase.dto;

import io.swagger.annotations.ApiModelProperty;
import org.jeecg.modules.purchase.entity.PurchaseReturnMtl;

import java.util.List;

/**
 * @Description:
 * @Author: tomkluas
 * @Date: 2020/1/17 11:35
 * @Version: V1.0
 */
public class PurchaseReturnInDto {

    @ApiModelProperty("出库单ID")
    private String id;

    @ApiModelProperty("采购单ID")
    private String sourceId;

    @ApiModelProperty("出货时间")
    private String putOutTime;

    private List<PurchaseReturnMtl> detaillist;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPutOutTime() {
        return putOutTime;
    }

    public void setPutOutTime(String putOutTime) {
        this.putOutTime = putOutTime;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public List<PurchaseReturnMtl> getDetaillist() {
        return detaillist;
    }

    public void setDetaillist(List<PurchaseReturnMtl> detaillist) {
        this.detaillist = detaillist;
    }
}
