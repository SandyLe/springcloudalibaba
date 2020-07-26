package org.jeecg.modules.inventory.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;
import java.util.List;

@ApiModel( value = "PreInventoryDto", description = "出入库DTO")
public class PreInventoryDto {

    @ApiModelProperty("出入库方式")
    private Integer deliveryTypeId;

    @ApiModelProperty("物流公司")
    private String logisticsId;

    @ApiModelProperty("物流单号")
    private String logisticsNo;

    @ApiModelProperty("出入库时间")
    private Date deliveryDate;

    @ApiModelProperty("出入库产品")
    private List<PreInventoryOutMtl> mtls;

    public Integer getDeliveryTypeId() {
        return deliveryTypeId;
    }

    public void setDeliveryTypeId(Integer deliveryTypeId) {
        this.deliveryTypeId = deliveryTypeId;
    }

    public String getLogisticsNo() {
        return logisticsNo;
    }

    public void setLogisticsNo(String logisticsNo) {
        this.logisticsNo = logisticsNo;
    }

    public Date getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(Date deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public List<PreInventoryOutMtl> getMtls() {
        return mtls;
    }

    public void setMtls(List<PreInventoryOutMtl> mtls) {
        this.mtls = mtls;
    }

    public String getLogisticsId() {
        return logisticsId;
    }

    public void setLogisticsId(String logisticsId) {
        this.logisticsId = logisticsId;
    }
}
