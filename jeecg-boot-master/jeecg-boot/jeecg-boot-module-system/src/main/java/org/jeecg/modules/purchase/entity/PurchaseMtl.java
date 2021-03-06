package org.jeecg.modules.purchase.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang.StringUtils;
import org.jeecg.modules.basic.entity.CBasicEntity;
import java.math.BigDecimal;

/**
 * @Description:
 * @Author: tomkluas
 * @Date: 2020/1/4 14:33
 * @Version: V1.0
 */
@TableName("sl_purchase_mtl")
@ApiModel(value = "PurchaseMtl", description = "采购订单产品明细")
public class PurchaseMtl extends CBasicEntity {

    /*采购订单id*/
    @ApiModelProperty("采购订单id")
    private String sourceId;

    /*产品id*/
    @ApiModelProperty("产品id")
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
    @ApiModelProperty("辅助属性ID")
    private String auxiliaryId;

    /*单位*/
    @ApiModelProperty("单位")
    private String unitId;

    /*数量*/
    @ApiModelProperty("数量")
    private BigDecimal quantity;

    /*单价*/
    @ApiModelProperty("单价")
    private BigDecimal price;

    /*折扣*/
    @ApiModelProperty("折扣")
    private BigDecimal discount;

    /*金额*/
    @ApiModelProperty("金额")
    private BigDecimal amount;
    @TableField(exist=false)
    private String info;


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

    public String getMtl() {
        return mtl;
    }

    public void setMtl(String mtl) {
        this.mtl = mtl;
    }

    public String getMtlCode() {
        return mtlCode;
    }

    public void setMtlCode(String mtlCode) {
        this.mtlCode = mtlCode;
    }

    public String getSpecification() {
        return specification;
    }

    public void setSpecification(String specification) {
        this.specification = specification;
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
}
