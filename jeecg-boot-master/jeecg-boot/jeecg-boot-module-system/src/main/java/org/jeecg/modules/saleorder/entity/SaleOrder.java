package org.jeecg.modules.saleorder.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecg.modules.basic.entity.CBasicEntity;

import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("sl_sale_order")
public class SaleOrder extends CBasicEntity {

    @ApiModelProperty("客户ID")
    private String customerId;
    @TableField(exist=false)
    @ApiModelProperty("客户")
    private String customer;
    @ApiModelProperty("渠道")
    private String channelId;
    @ApiModelProperty("渠道")
    @TableField(exist=false)
    private String channel;
    @ApiModelProperty("送货时间")
    private Date deliveryTime;
    @ApiModelProperty("安装时间")
    private Date installTime;
    @ApiModelProperty("测量时间")
    private Date measuringTime;
    @ApiModelProperty("收款状态")
    private Integer receiptStatus;
    @ApiModelProperty("收款状态名称")
    @TableField(exist=false)
    private String receiptStatusName;
    @ApiModelProperty("出库方式")
    private Integer deliveryType;
    @ApiModelProperty("出库方式名称")
    @TableField(exist=false)
    private String deliveryTypeName;
    @ApiModelProperty("订单时间")
    private Date billDate;
    @ApiModelProperty("订单状态")
    private Integer billStatus;
    @ApiModelProperty("订单状态")
    @TableField(exist=false)
    private String billStatusName;
    @ApiModelProperty("产品费用金额")
    private BigDecimal mtlamount;
    @ApiModelProperty("其他费用")
    private BigDecimal otheramount;
    @ApiModelProperty("总金额")
    private BigDecimal totalamount;
    @ApiModelProperty("已付金额")
    private BigDecimal payamount;
    @ApiModelProperty("销售员ID")
    private String salemanId;
    @TableField(exist=false)
    @ApiModelProperty("销售员")
    private String salemanName;
    @ApiModelProperty("单据类型")
    private Integer billType;

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public Date getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(Date deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public Date getInstallTime() {
        return installTime;
    }

    public void setInstallTime(Date installTime) {
        this.installTime = installTime;
    }

    public Date getBillDate() {
        return billDate;
    }

    public void setBillDate(Date billDate) {
        this.billDate = billDate;
    }

    public Integer getBillStatus() {
        return billStatus;
    }

    public void setBillStatus(Integer billStatus) {
        this.billStatus = billStatus;
    }

    public BigDecimal getTotalamount() {
        return totalamount;
    }

    public void setTotalamount(BigDecimal totalamount) {
        this.totalamount = totalamount;
    }

    public BigDecimal getPayamount() {
        return payamount;
    }

    public void setPayamount(BigDecimal payamount) {
        this.payamount = payamount;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public BigDecimal getMtlamount() {
        return mtlamount;
    }

    public void setMtlamount(BigDecimal mtlamount) {
        this.mtlamount = mtlamount;
    }

    public BigDecimal getOtheramount() {
        return otheramount;
    }

    public void setOtheramount(BigDecimal otheramount) {
        this.otheramount = otheramount;
    }

    public String getBillStatusName() {
        return billStatusName;
    }

    public void setBillStatusName(String billStatusName) {
        this.billStatusName = billStatusName;
    }

    public Date getMeasuringTime() {
        return measuringTime;
    }

    public void setMeasuringTime(Date measuringTime) {
        this.measuringTime = measuringTime;
    }

    public Integer getReceiptStatus() {
        return receiptStatus;
    }

    public void setReceiptStatus(Integer receiptStatus) {
        this.receiptStatus = receiptStatus;
    }

    public String getReceiptStatusName() {
        return receiptStatusName;
    }

    public void setReceiptStatusName(String receiptStatusName) {
        this.receiptStatusName = receiptStatusName;
    }

    public Integer getDeliveryType() {
        return deliveryType;
    }

    public void setDeliveryType(Integer deliveryType) {
        this.deliveryType = deliveryType;
    }

    public String getDeliveryTypeName() {
        return deliveryTypeName;
    }

    public void setDeliveryTypeName(String deliveryTypeName) {
        this.deliveryTypeName = deliveryTypeName;
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

    public Integer getBillType() {
        return billType;
    }

    public void setBillType(Integer billType) {
        this.billType = billType;
    }
}
