package org.jeecg.modules.saleorder.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecg.modules.basic.entity.BasicEntity;

import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("sl_sale_order")
public class SaleOrder extends BasicEntity {

    @ApiModelProperty("客户ID")
    private String customerId;
    @TableField(exist=false)
    @ApiModelProperty("客户")
    private String customer;
    @ApiModelProperty("出货仓库ID")
    private String warehouseId;
    @ApiModelProperty("出货仓库")
    @TableField(exist=false)
    private String warehouse;
    @ApiModelProperty("渠道")
    private String channelId;
    @ApiModelProperty("渠道")
    @TableField(exist=false)
    private String channel;
    @ApiModelProperty("发货时间")
    private Date deliveryTime;
    @ApiModelProperty("安装时间")
    private Date installTime;
    @ApiModelProperty("收款方式")
    private String receiptType;
    @ApiModelProperty("开票信息")
    private String billingInfo;
    @ApiModelProperty("订单时间")
    private Date billDate;
    @ApiModelProperty("订单状态")
    private Integer billStatus;
    @ApiModelProperty("产品费用金额")
    private BigDecimal mtlamount;
    @ApiModelProperty("其他费用")
    private BigDecimal otheramount;
    @ApiModelProperty("总金额")
    private BigDecimal totalamount;
    @ApiModelProperty("已付金额")
    private BigDecimal payamount;

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(String warehouseId) {
        this.warehouseId = warehouseId;
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

    public String getReceiptType() {
        return receiptType;
    }

    public void setReceiptType(String receiptType) {
        this.receiptType = receiptType;
    }

    public String getBillingInfo() {
        return billingInfo;
    }

    public void setBillingInfo(String billingInfo) {
        this.billingInfo = billingInfo;
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

    public String getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(String warehouse) {
        this.warehouse = warehouse;
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
}
