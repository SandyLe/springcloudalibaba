package org.jeecg.modules.saleorder.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecg.modules.basic.entity.BasicEntity;
import org.jeecg.modules.basic.entity.CBasicEntity;

import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("sl_sale_order_return")
public class SaleOrderReturn extends CBasicEntity {

    @ApiModelProperty("客户ID")
    private String customerId;
    @ApiModelProperty("原单ID")
    private String sourceId;
    @ApiModelProperty("原单编号")
    private String sourceCode;
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
    @ApiModelProperty("付款方式")
    private String payType;
    @ApiModelProperty("付款方式")
    @TableField(exist=false)
    private String payTypeName;
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
    @ApiModelProperty("已退金额")
    private BigDecimal payamount;
    @ApiModelProperty("单据类型")
    private Integer billType;
    @ApiModelProperty("收款状态")
    private Integer refundStatusId;
    @ApiModelProperty("收款状态名称")
    @TableField(exist=false)
    private String refundStatusName;
    @ApiModelProperty("业务员ID")
    private String salemanId;
    @TableField(exist=false)
    private String salemanName;

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public String getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(String warehouseId) {
        this.warehouseId = warehouseId;
    }

    public String getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(String warehouse) {
        this.warehouse = warehouse;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getPayTypeName() {
        return payTypeName;
    }

    public void setPayTypeName(String payTypeName) {
        this.payTypeName = payTypeName;
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

    public String getBillStatusName() {
        return billStatusName;
    }

    public void setBillStatusName(String billStatusName) {
        this.billStatusName = billStatusName;
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

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public String getSourceCode() {
        return sourceCode;
    }

    public void setSourceCode(String sourceCode) {
        this.sourceCode = sourceCode;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public Integer getBillType() {
        return billType;
    }

    public void setBillType(Integer billType) {
        this.billType = billType;
    }

    public Integer getRefundStatusId() {
        return refundStatusId;
    }

    public void setRefundStatusId(Integer refundStatusId) {
        this.refundStatusId = refundStatusId;
    }

    public String getRefundStatusName() {
        return refundStatusName;
    }

    public void setRefundStatusName(String refundStatusName) {
        this.refundStatusName = refundStatusName;
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
}
