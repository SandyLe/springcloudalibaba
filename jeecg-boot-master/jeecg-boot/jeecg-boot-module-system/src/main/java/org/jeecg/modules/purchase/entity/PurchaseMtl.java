package org.jeecg.modules.purchase.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.jeecg.modules.basic.entity.BasicEntity;
import org.springframework.format.annotation.DateTimeFormat;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Description:
 * @Author: tomkluas
 * @Date: 2020/1/4 14:33
 * @Version: V1.0
 */
@TableName("sl_purchase_mtl")
@ApiModel(value = "PurchaseMtl", description = "采购订单产品明细")
public class PurchaseMtl extends BasicEntity {

    /*采购订单id*/
    @ApiModelProperty("采购订单id")
    private String sourceId;

    /*产品id*/
    @ApiModelProperty("产品id")
    private String mtlId;
    @ApiModelProperty("产品")
    @TableField(exist=false)
    private String mtl;

    /*单位*/
    @ApiModelProperty("单位")
    private String unitId;

    /*数量*/
    @ApiModelProperty("数量")
    private BigDecimal quantity;

    /*单价*/
    @ApiModelProperty("单价")
    private String price;

    /*折扣*/
    @ApiModelProperty("折扣")
    private String discount;

    /*金额*/
    @ApiModelProperty("金额")
    private String amount;


    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public String getMtlId() {
        return mtlId;
    }

    public void setMtlId(String mtlId) {
        this.mtlId = mtlId;
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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getMtl() {
        return mtl;
    }

    public void setMtl(String mtl) {
        this.mtl = mtl;
    }
}
