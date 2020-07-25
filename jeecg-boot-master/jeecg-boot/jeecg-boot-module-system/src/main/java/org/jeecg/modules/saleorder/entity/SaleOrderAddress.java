package org.jeecg.modules.saleorder.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.jeecg.modules.basic.entity.CBasicEntity;

@TableName("sl_sale_order_address")
@ApiModel(value = "SaleOrderAddress", description = "销售订单功能地址")
public class SaleOrderAddress extends CBasicEntity {


    @ApiModelProperty("所属对象")
    private String sourceId;
    @ApiModelProperty("原客户地址ID")
    private String sourceAddId;
    @ApiModelProperty("地址类型")
    private Integer typeId;
    @TableField(exist=false)
    private String typeName;
    @ApiModelProperty("省")
    private String province;
    @ApiModelProperty("市")
    private String city;
    @ApiModelProperty("区、县")
    private String district;
    @ApiModelProperty("电话")
    private String tel;
    @ApiModelProperty("收件人")
    private String recipients;
    @TableField(exist=false)
    private String fullAddress;

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getRecipients() {
        return recipients;
    }

    public void setRecipients(String recipients) {
        this.recipients = recipients;
    }

    public String getFullAddress() {
        return fullAddress;
    }

    public void setFullAddress(String fullAddress) {
        this.fullAddress = fullAddress;
    }

    public String getSourceAddId() {
        return sourceAddId;
    }

    public void setSourceAddId(String sourceAddId) {
        this.sourceAddId = sourceAddId;
    }
}
