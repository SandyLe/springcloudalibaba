package org.jeecg.modules.purchase.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.jeecg.modules.basic.entity.BasicEntity;

import java.math.BigDecimal;

/**
 * @Description:
 * @Author: tomkluas
 * @Date: 2020/1/4 14:33
 * @Version: V1.0
 */
@TableName("sl_purchase_return_dtl")
@ApiModel(value = "PurchaseReturnDtl", description = "采购退货明细")
public class PurchaseReturnDtl extends BasicEntity {

    /*采购订单id*/
    @ApiModelProperty("采购订单id")
    private String sourceId;

    /*产品id*/
    @ApiModelProperty("产品id")
    private String mtlId;

    /*单位*/
    @ApiModelProperty("单位")
    private String unitId;

    /*数量*/
    @ApiModelProperty("数量")
    private String quantity;

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

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
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
}
