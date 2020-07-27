package org.jeecg.modules.invoice.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecg.modules.basic.entity.CBasicEntity;

import java.math.BigDecimal;
import java.util.Date;

@ApiModel(value = "Invoice", description = "发票")
@Data
@TableName("sl_invoice")
public class Invoice extends CBasicEntity {

    @ApiModelProperty("原单类型")
    private Integer billType;
    @ApiModelProperty("原单类型")
    private Integer sourceBillType;
    @ApiModelProperty("原单类型名称")
    @TableField(exist=false)
    private String sourceBillTypeName;
    @ApiModelProperty("销售订单ID")
    private String sourceId;
    @ApiModelProperty("原单Code")
    private String sourceCode;
    @ApiModelProperty("发票类型")
    private Integer invoiceTypeId;
    @ApiModelProperty("发票类型名称")
    @TableField(exist=false)
    private String invoiceTypeName;
    @ApiModelProperty("发票材质：0纸质；1电子")
    private Integer invoiceTextureId;
    @ApiModelProperty("发票材质：0纸质；1电子")
    @TableField(exist=false)
    private String invoiceTextureName;

    @ApiModelProperty("抬头")
    private String billTitle;
    @ApiModelProperty("税号")
    private String taxNo;
    @ApiModelProperty("电子邮件")
    private String email;
    @ApiModelProperty("地址")
    private String address;
    @ApiModelProperty("电话")
    private String tel;
    @ApiModelProperty("开户行")
    private String openningBank;
    @ApiModelProperty("账号")
    private String bankNo;

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

    @ApiModelProperty("发票号")
    private String invoiceNo;

    public Integer getBillType() {
        return billType;
    }

    public void setBillType(Integer billType) {
        this.billType = billType;
    }

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

    public String getSourceCode() {
        return sourceCode;
    }

    public void setSourceCode(String sourceCode) {
        this.sourceCode = sourceCode;
    }

    public Integer getInvoiceTypeId() {
        return invoiceTypeId;
    }

    public void setInvoiceTypeId(Integer invoiceTypeId) {
        this.invoiceTypeId = invoiceTypeId;
    }

    public String getInvoiceTypeName() {
        return invoiceTypeName;
    }

    public void setInvoiceTypeName(String invoiceTypeName) {
        this.invoiceTypeName = invoiceTypeName;
    }

    public Integer getInvoiceTextureId() {
        return invoiceTextureId;
    }

    public void setInvoiceTextureId(Integer invoiceTextureId) {
        this.invoiceTextureId = invoiceTextureId;
    }

    public String getInvoiceTextureName() {
        return invoiceTextureName;
    }

    public void setInvoiceTextureName(String invoiceTextureName) {
        this.invoiceTextureName = invoiceTextureName;
    }

    public String getBillTitle() {
        return billTitle;
    }

    public void setBillTitle(String billTitle) {
        this.billTitle = billTitle;
    }

    public String getTaxNo() {
        return taxNo;
    }

    public void setTaxNo(String taxNo) {
        this.taxNo = taxNo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getOpenningBank() {
        return openningBank;
    }

    public void setOpenningBank(String openningBank) {
        this.openningBank = openningBank;
    }

    public String getBankNo() {
        return bankNo;
    }

    public void setBankNo(String bankNo) {
        this.bankNo = bankNo;
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

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }
}
