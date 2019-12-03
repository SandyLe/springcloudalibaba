package org.jeecg.modules.basic.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@TableName("sl_customer_delivery_info")
@ApiModel(value = "CustomerDeliveryInfo", description = "客户发货信息")
public class CustomerDeliveryInfo extends BasicEntity {

    @ApiModelProperty("客户")
    private String cdi_sourceId;
    @ApiModelProperty("发货方式")
    private String cdi_defaultType;
    @ApiModelProperty("说明")
    private String cdi_description;
    @ApiModelProperty("联系人")
    private String cdi_linkman;
    @ApiModelProperty("联系电话")
    private String cdi_phone;
    @ApiModelProperty("车牌号")
    private String cdi_carLicense;
    @ApiModelProperty("发货地址")
    private String cdi_deliveryAddress;
    @ApiModelProperty("省")
    private String cdi_province;
    @ApiModelProperty("市")
    private String cdi_city;
    @ApiModelProperty("区、县")
    private String cdi_district;
    @ApiModelProperty("详细地址")
    private String cdi_address;
    @ApiModelProperty("物流公司")
    private String cdi_logistics;
    @ApiModelProperty("网点")
    private String cdi_branch;
    @ApiModelProperty("电话")
    private String cdi_tel;

    public String getCdi_sourceId() {
        return cdi_sourceId;
    }

    public void setCdi_sourceId(String cdi_sourceId) {
        this.cdi_sourceId = cdi_sourceId;
    }

    public String getCdi_defaultType() {
        return cdi_defaultType;
    }

    public void setCdi_defaultType(String cdi_defaultType) {
        this.cdi_defaultType = cdi_defaultType;
    }

    public String getCdi_description() {
        return cdi_description;
    }

    public void setCdi_description(String cdi_description) {
        this.cdi_description = cdi_description;
    }

    public String getCdi_linkman() {
        return cdi_linkman;
    }

    public void setCdi_linkman(String cdi_linkman) {
        this.cdi_linkman = cdi_linkman;
    }

    public String getCdi_phone() {
        return cdi_phone;
    }

    public void setCdi_phone(String cdi_phone) {
        this.cdi_phone = cdi_phone;
    }

    public String getCdi_carLicense() {
        return cdi_carLicense;
    }

    public void setCdi_carLicense(String cdi_carLicense) {
        this.cdi_carLicense = cdi_carLicense;
    }

    public String getCdi_deliveryAddress() {
        return cdi_deliveryAddress;
    }

    public void setCdi_deliveryAddress(String cdi_deliveryAddress) {
        this.cdi_deliveryAddress = cdi_deliveryAddress;
    }

    public String getCdi_province() {
        return cdi_province;
    }

    public void setCdi_province(String cdi_province) {
        this.cdi_province = cdi_province;
    }

    public String getCdi_city() {
        return cdi_city;
    }

    public void setCdi_city(String cdi_city) {
        this.cdi_city = cdi_city;
    }

    public String getCdi_district() {
        return cdi_district;
    }

    public void setCdi_district(String cdi_district) {
        this.cdi_district = cdi_district;
    }

    public String getCdi_address() {
        return cdi_address;
    }

    public void setCdi_address(String cdi_address) {
        this.cdi_address = cdi_address;
    }

    public String getCdi_logistics() {
        return cdi_logistics;
    }

    public void setCdi_logistics(String cdi_logistics) {
        this.cdi_logistics = cdi_logistics;
    }

    public String getCdi_branch() {
        return cdi_branch;
    }

    public void setCdi_branch(String cdi_branch) {
        this.cdi_branch = cdi_branch;
    }

    public String getCdi_tel() {
        return cdi_tel;
    }

    public void setCdi_tel(String cdi_tel) {
        this.cdi_tel = cdi_tel;
    }
}
