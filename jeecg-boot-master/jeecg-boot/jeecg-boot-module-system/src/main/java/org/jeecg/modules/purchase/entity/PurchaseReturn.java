package org.jeecg.modules.purchase.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.jeecg.modules.basic.entity.CBasicEntity;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;


@TableName("sl_purchase_return")
@ApiModel(value = "Purchasereturn", description = "采购退货")
public class PurchaseReturn extends CBasicEntity {

    /*供应商*/
    @Excel(name = "供应商", width = 15)
    @ApiModelProperty("供应商")
    private String vendorId;
    @ApiModelProperty("原单编号")
    private String sourceCode;
    @TableField(exist=false)
    private String vendor;
    @TableField(exist=false)
    private String warehouse;
    /*结算账户*/
    @Excel(name = "结算账户",width = 20)
    @ApiModelProperty("结算账户")
    private String account;

    /*仓库id*/
    @Excel(name = "仓库id", width = 15)
    @ApiModelProperty("仓库id")
    private String warehouseId;

    /*金额*/
    @Excel(name = "金额",width = 20)
    @ApiModelProperty("金额")
    private String amount;

    /*订单日期*/
    @Excel(name = "订单日期",width = 20 , format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("订单日期")
    private Date billdate;

    /*原单id*/
    @Excel(name = "原单id", width = 15)
    @ApiModelProperty("原单id")
    private String sourceId;
    @ApiModelProperty("订单状态")
    private Integer billStatus;
    @ApiModelProperty("订单状态")
    @TableField(exist=false)
    private String billStatusName;
    @ApiModelProperty("单据类型")
    private Integer billType;
    @TableField(exist=false)
    @ApiModelProperty("单据类型名称")
    private String billTypeName;

    public String getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(String warehouseId) {
        this.warehouseId = warehouseId;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public Date getBilldate() {
        return billdate;
    }

    public void setBilldate(Date billdate) {
        this.billdate = billdate;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
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

    public String getVendorId() {
        return vendorId;
    }

    public void setVendorId(String vendorId) {
        this.vendorId = vendorId;
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public String getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(String warehouse) {
        this.warehouse = warehouse;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getSourceCode() {
        return sourceCode;
    }

    public void setSourceCode(String sourceCode) {
        this.sourceCode = sourceCode;
    }

    public Integer getBillType() {
        return billType;
    }

    public void setBillType(Integer billType) {
        this.billType = billType;
    }

    public String getBillTypeName() {
        return billTypeName;
    }

    public void setBillTypeName(String billTypeName) {
        this.billTypeName = billTypeName;
    }
}
