package org.jeecg.modules.saleorder.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecg.common.system.base.entity.JeecgEntity;

import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("sl_sale_order_mtl")
public class SaleOrderMtl extends JeecgEntity {

    @ApiModelProperty("销售订单ID")
    private String sourceId;
    @ApiModelProperty("产品ID")
    private String mtlId;
    @ApiModelProperty("产品")
    @TableField(exist=false)
    private String mtl;
    @ApiModelProperty("单位ID")
    private String unitId;
    @ApiModelProperty("单位")
    @TableField(exist=false)
    private String unit;
    @ApiModelProperty("数量")
    private BigDecimal quantity;
    @ApiModelProperty("单价")
    private BigDecimal price;
    @ApiModelProperty("折扣")
    private BigDecimal discount;
    @ApiModelProperty("折扣类型")
    private String discountType;
    @ApiModelProperty("金额")
    private BigDecimal amount;

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
}
