package org.jeecg.modules.saleorder.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.jeecg.modules.basic.entity.BasicEntity;

@TableName("sl_saleorder_delivery_info")
@ApiModel(value = "SaleOrderDeliveryInfo", description = "客户发货信息")
public class SaleOrderDeliveryInfo extends BasicEntity {

    @ApiModelProperty("销售订单ID")
    private String sourceId;
    @ApiModelProperty("销售订单Code")
    private String sourceBillCode;
    @ApiModelProperty("客户ID")
    private String cdiSourceId;
    @ApiModelProperty("客户")
    @TableField(exist=false)
    private String customer;
    @ApiModelProperty("发货方式")
    private String cdiDefaultType;
    @ApiModelProperty("发货方式")
    @TableField(exist=false)
    private String cdiDefaultTypeName;
    @ApiModelProperty("出货仓库ID")
    private String warehouseId;
    @ApiModelProperty("出货仓库")
    @TableField(exist=false)
    private String warehouse;
    @ApiModelProperty("说明")
    private String cdiDescription;
    @ApiModelProperty("联系人")
    private String cdiLinkman;
    @ApiModelProperty("联系电话")
    private String cdiPhone;
    @ApiModelProperty("车牌号")
    private String cdiCarLicense;
    @ApiModelProperty("发货地址")
    private String cdiDeliveryAddress;
    @ApiModelProperty("省")
    private String cdiProvince;
    @ApiModelProperty("市")
    private String cdiCity;
    @ApiModelProperty("区、县")
    private String cdiDistrict;
    @ApiModelProperty("详细地址")
    private String cdiAddress;
    @ApiModelProperty("物流公司")
    private String cdiLogistics;
    @ApiModelProperty("网点")
    private String cdiBranch;
    @ApiModelProperty("电话")
    private String cdiTel;
    @ApiModelProperty("收件人联系电话")
    private String cdiRecipientsPhone;
    @ApiModelProperty("收件人")
    private String cdiRecipients;
    @ApiModelProperty("表单状态")
    private Integer billStatus;
    @ApiModelProperty("订单状态")
    @TableField(exist=false)
    private String billStatusName;

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

    public String getCdiSourceId() {
        return cdiSourceId;
    }

    public void setCdiSourceId(String cdiSourceId) {
        this.cdiSourceId = cdiSourceId;
    }

    public String getCdiDefaultType() {
        return cdiDefaultType;
    }

    public void setCdiDefaultType(String cdiDefaultType) {
        this.cdiDefaultType = cdiDefaultType;
    }

    public String getCdiDescription() {
        return cdiDescription;
    }

    public void setCdiDescription(String cdiDescription) {
        this.cdiDescription = cdiDescription;
    }

    public String getCdiLinkman() {
        return cdiLinkman;
    }

    public void setCdiLinkman(String cdiLinkman) {
        this.cdiLinkman = cdiLinkman;
    }

    public String getCdiPhone() {
        return cdiPhone;
    }

    public void setCdiPhone(String cdiPhone) {
        this.cdiPhone = cdiPhone;
    }

    public String getCdiCarLicense() {
        return cdiCarLicense;
    }

    public void setCdiCarLicense(String cdiCarLicense) {
        this.cdiCarLicense = cdiCarLicense;
    }

    public String getCdiDeliveryAddress() {
        return cdiDeliveryAddress;
    }

    public void setCdiDeliveryAddress(String cdiDeliveryAddress) {
        this.cdiDeliveryAddress = cdiDeliveryAddress;
    }

    public String getCdiProvince() {
        return cdiProvince;
    }

    public void setCdiProvince(String cdiProvince) {
        this.cdiProvince = cdiProvince;
    }

    public String getCdiCity() {
        return cdiCity;
    }

    public void setCdiCity(String cdiCity) {
        this.cdiCity = cdiCity;
    }

    public String getCdiDistrict() {
        return cdiDistrict;
    }

    public void setCdiDistrict(String cdiDistrict) {
        this.cdiDistrict = cdiDistrict;
    }

    public String getCdiAddress() {
        return cdiAddress;
    }

    public void setCdiAddress(String cdiAddress) {
        this.cdiAddress = cdiAddress;
    }

    public String getCdiLogistics() {
        return cdiLogistics;
    }

    public void setCdiLogistics(String cdiLogistics) {
        this.cdiLogistics = cdiLogistics;
    }

    public String getCdiBranch() {
        return cdiBranch;
    }

    public void setCdiBranch(String cdiBranch) {
        this.cdiBranch = cdiBranch;
    }

    public String getCdiTel() {
        return cdiTel;
    }

    public void setCdiTel(String cdiTel) {
        this.cdiTel = cdiTel;
    }

    public String getCdiRecipientsPhone() {
        return cdiRecipientsPhone;
    }

    public void setCdiRecipientsPhone(String cdiRecipientsPhone) {
        this.cdiRecipientsPhone = cdiRecipientsPhone;
    }

    public String getCdiRecipients() {
        return cdiRecipients;
    }

    public void setCdiRecipients(String cdiRecipients) {
        this.cdiRecipients = cdiRecipients;
    }

    public String getSourceBillCode() {
        return sourceBillCode;
    }

    public void setSourceBillCode(String sourceBillCode) {
        this.sourceBillCode = sourceBillCode;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public String getCdiDefaultTypeName() {
        return cdiDefaultTypeName;
    }

    public void setCdiDefaultTypeName(String cdiDefaultTypeName) {
        this.cdiDefaultTypeName = cdiDefaultTypeName;
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

    public String getBillStatusName() {
        return billStatusName;
    }

    public void setBillStatusName(String billStatusName) {
        this.billStatusName = billStatusName;
    }
}
