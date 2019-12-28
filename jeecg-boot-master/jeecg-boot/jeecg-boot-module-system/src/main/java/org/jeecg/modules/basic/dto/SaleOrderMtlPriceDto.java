package org.jeecg.modules.basic.dto;

import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;

public class SaleOrderMtlPriceDto {

    @ApiModelProperty(value = "产品ID")
    private String mtlId;
    @ApiModelProperty(value = "产品名称")
    private String mtlName;
    @ApiModelProperty(value = "产品Code")
    private String mtlCode;
    @ApiModelProperty(value = "单位ID")
    private String unitId;
    @ApiModelProperty(value = "规格")
    private String specification;
    @ApiModelProperty(value = "价格")
    private BigDecimal price;

    public String getMtlId() {
        return mtlId;
    }

    public void setMtlId(String mtlId) {
        this.mtlId = mtlId;
    }

    public String getMtlName() {
        return mtlName;
    }

    public void setMtlName(String mtlName) {
        this.mtlName = mtlName;
    }

    public String getMtlCode() {
        return mtlCode;
    }

    public void setMtlCode(String mtlCode) {
        this.mtlCode = mtlCode;
    }

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    public String getSpecification() {
        return specification;
    }

    public void setSpecification(String specification) {
        this.specification = specification;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
