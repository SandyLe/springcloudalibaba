package org.jeecg.modules.financial.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecg.modules.basic.entity.CBasicEntity;

import java.math.BigDecimal;
import java.util.Date;

@ApiModel(value = "ReceiptOrder", description = "收款单")
@Data
@TableName("sl_receipt_order")
public class ReceiptOrder extends CBasicEntity {

    @ApiModelProperty("原单类型")
    private Integer sourceBillType;
    @ApiModelProperty("原单类型名称")
    @TableField(exist=false)
    private String sourceBillTypeName;
    @ApiModelProperty("销售订单ID")
    private String sourceId;
    @ApiModelProperty("原单Code")
    private String sourceBillCode;
    @ApiModelProperty("付款方ID")
    private String payerId;
    @ApiModelProperty("付款方")
    @TableField(exist=false)
    private String payerName;
    @ApiModelProperty("金额")
    private BigDecimal amount;
    @ApiModelProperty("订单状态")
    private Integer billStatusId;
    @ApiModelProperty("订单状态")
    @TableField(exist=false)
    private String billStatusName;
    @ApiModelProperty("业务员ID")
    private String salemanId;
    @ApiModelProperty("业务员")
    @TableField(exist=false)
    private String salemanName;
    @ApiModelProperty("单据时间")
    private Date billDate;
    @ApiModelProperty("出库单状态")
    @TableField(exist=false)
    private String inventOutStatus;

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

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public String getPayerId() {
        return payerId;
    }

    public void setPayerId(String payerId) {
        this.payerId = payerId;
    }

    public String getPayerName() {
        return payerName;
    }

    public void setPayerName(String payerName) {
        this.payerName = payerName;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Integer getBillStatusId() {
        return billStatusId;
    }

    public void setBillStatusId(Integer billStatusId) {
        this.billStatusId = billStatusId;
    }

    public String getBillStatusName() {
        return billStatusName;
    }

    public void setBillStatusName(String billStatusName) {
        this.billStatusName = billStatusName;
    }

    public String getSalemanId() {
        return salemanId;
    }

    public void setSalemanId(String salemanId) {
        this.salemanId = salemanId;
    }

    public String getSalemanName() {
        return salemanName;
    }

    public void setSalemanName(String salemanName) {
        this.salemanName = salemanName;
    }

    public Date getBillDate() {
        return billDate;
    }

    public void setBillDate(Date billDate) {
        this.billDate = billDate;
    }

    public String getSourceBillCode() {
        return sourceBillCode;
    }

    public void setSourceBillCode(String sourceBillCode) {
        this.sourceBillCode = sourceBillCode;
    }

    public String getInventOutStatus() {
        return inventOutStatus;
    }

    public void setInventOutStatus(String inventOutStatus) {
        this.inventOutStatus = inventOutStatus;
    }
}
