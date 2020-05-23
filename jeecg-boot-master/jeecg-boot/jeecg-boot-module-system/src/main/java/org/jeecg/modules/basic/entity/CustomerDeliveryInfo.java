package org.jeecg.modules.basic.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@TableName("sl_customer_delivery_info")
@ApiModel(value = "CustomerDeliveryInfo", description = "客户发货信息")
public class CustomerDeliveryInfo extends CBasicEntity {

    @ApiModelProperty("客户")
    private String cdiSourceId;
    @ApiModelProperty("发货方式")
    private String cdiDefaultType;
    @TableField(exist=false)
    private String cdiDefaultTypeName;
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
    private String cdiLogisticsId;
    @TableField(exist=false)
    private String cdiLogisticsName;
    @ApiModelProperty("网点")
    private String cdiBranch;
    @ApiModelProperty("电话")
    private String cdiTel;
    @ApiModelProperty("收件人联系电话")
    private String cdiRecipientsPhone;
    @ApiModelProperty("收件人")
    private String cdiRecipients;
    @TableField(exist=false)
    private String cdiFullAddress;

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

    public String getCdiLogisticsId() {
        return cdiLogisticsId;
    }

    public void setCdiLogisticsId(String cdiLogisticsId) {
        this.cdiLogisticsId = cdiLogisticsId;
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

    public String getCdiDefaultTypeName() {
        return cdiDefaultTypeName;
    }

    public void setCdiDefaultTypeName(String cdiDefaultTypeName) {
        this.cdiDefaultTypeName = cdiDefaultTypeName;
    }

    public String getCdiFullAddress() {
        return cdiFullAddress;
    }

    public void setCdiFullAddress(String cdiFullAddress) {
        this.cdiFullAddress = cdiFullAddress;
    }

    public String getCdiLogisticsName() {
        return cdiLogisticsName;
    }

    public void setCdiLogisticsName(String cdiLogisticsName) {
        this.cdiLogisticsName = cdiLogisticsName;
    }
}
