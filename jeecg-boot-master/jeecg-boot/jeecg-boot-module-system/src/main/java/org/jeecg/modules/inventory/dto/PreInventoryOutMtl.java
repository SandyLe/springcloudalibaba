package org.jeecg.modules.inventory.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;

public class PreInventoryOutMtl {

    @ApiModelProperty("销售出库ID")
    private String billId;
    @ApiModelProperty("销售订单ID")
    private String sourceId;
    @ApiModelProperty("原单类型")
    private Integer sourceBillType;
    @TableField(exist=false)
    private String sourceBillTypeName;
    @ApiModelProperty("产品ID")
    private String mtlId;
    @ApiModelProperty("产品")
    @TableField(exist=false)
    private String mtl;
    @ApiModelProperty("产品Code")
    @TableField(exist=false)
    private String mtlCode;
    @ApiModelProperty("规格")
    @TableField(exist=false)
    private String specification;
    @ApiModelProperty("单位ID")
    private String unitId;
    @ApiModelProperty("单位")
    @TableField(exist=false)
    private String unit;
    @ApiModelProperty("数量")
    private BigDecimal quantity;
    @ApiModelProperty("操作类型")
    private Integer operationId;

    public String getBillId() {
        return billId;
    }

    public void setBillId(String billId) {
        this.billId = billId;
    }

    public String getMtlId() {
        return mtlId;
    }

    public void setMtlId(String mtlId) {
        this.mtlId = mtlId;
    }

    public String getMtl() {
        return mtl;
    }

    public void setMtl(String mtl) {
        this.mtl = mtl;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public String getSpecification() {
        return specification;
    }

    public void setSpecification(String specification) {
        this.specification = specification;
    }

    public String getMtlCode() {
        return mtlCode;
    }

    public void setMtlCode(String mtlCode) {
        this.mtlCode = mtlCode;
    }

    public Integer getSourceBillType() {
        return sourceBillType;
    }

    public void setSourceBillType(Integer sourceBillType) {
        this.sourceBillType = sourceBillType;
    }

    public String getSourceBillTypeName() {
        return sourceBillTypeName;
    }

    public void setSourceBillTypeName(String sourceBillTypeName) {
        this.sourceBillTypeName = sourceBillTypeName;
    }

    public Integer getOperationId() {
        return operationId;
    }

    public void setOperationId(Integer operationId) {
        this.operationId = operationId;
    }
}
