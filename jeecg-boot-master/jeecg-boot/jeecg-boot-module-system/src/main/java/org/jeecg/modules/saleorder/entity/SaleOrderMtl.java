package org.jeecg.modules.saleorder.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang.StringUtils;
import org.jeecg.modules.basic.entity.CBasicEntity;

import java.math.BigDecimal;

@Data
@TableName("sl_sale_order_mtl")
public class SaleOrderMtl extends CBasicEntity {

    @ApiModelProperty("销售订单ID")
    private String sourceId;
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
    @ApiModelProperty("单价")
    private BigDecimal price;
    @ApiModelProperty("成交单价")
    private BigDecimal transactionPrice;
    @ApiModelProperty("折扣")
    private BigDecimal discount;
    @ApiModelProperty("折扣类型")
    private String discountType;
    @ApiModelProperty("折扣类型名称")
    @TableField(exist=false)
    private String discountTypeName;
    @ApiModelProperty("金额")
    private BigDecimal amount;
    @ApiModelProperty("订单状态")
    private Integer billStatus;
    @TableField(exist=false)
    private String info;
    @ApiModelProperty("辅助属性ID")
    private String auxiliaryId;
    @ApiModelProperty("辅助属性中文值")
    @TableField(exist=false)
    private String suppValueMap;

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

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getDiscountType() {
        return discountType;
    }

    public void setDiscountType(String discountType) {
        this.discountType = discountType;
    }

    public String getDiscountTypeName() {
        return discountTypeName;
    }

    public void setDiscountTypeName(String discountTypeName) {
        this.discountTypeName = discountTypeName;
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

    public Integer getBillStatus() {
        return billStatus;
    }

    public void setBillStatus(Integer billStatus) {
        this.billStatus = billStatus;
    }

    public BigDecimal getTransactionPrice() {
        return transactionPrice;
    }

    public void setTransactionPrice(BigDecimal transactionPrice) {
        this.transactionPrice = transactionPrice;
    }

    public String getAuxiliaryId() {
        return auxiliaryId;
    }

    public void setAuxiliaryId(String auxiliaryId) {
        this.auxiliaryId = auxiliaryId;
    }

    public String getInfo() {
        StringBuilder sb = new StringBuilder();
        if (StringUtils.isNotBlank(this.getMtl())) {
            sb.append(this.getMtl() + " | ");
        }
        if (StringUtils.isNotBlank(this.getMtlCode())) {
            sb.append(this.getMtlCode() + " | ");
        }
        if (StringUtils.isNotBlank(this.getSpecification())) {
            sb.append(this.getSpecification());
        }
        return sb.toString();
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getSuppValueMap() {
        return suppValueMap;
    }

    public void setSuppValueMap(String suppValueMap) {
        this.suppValueMap = suppValueMap;
    }
}
