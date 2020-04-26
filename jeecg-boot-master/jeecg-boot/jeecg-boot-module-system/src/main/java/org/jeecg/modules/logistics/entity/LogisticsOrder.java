package org.jeecg.modules.logistics.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecg.common.enums.BillStatus;
import org.jeecg.common.enums.BillType;
import org.jeecg.common.enums.EnumConvertUtils;
import org.jeecg.modules.basic.entity.CBasicEntity;

import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("sl_logistics_order")
@ApiModel(value = "LogisticsOrder", description = "物流定单")
public class LogisticsOrder extends CBasicEntity {

    @ApiModelProperty("原单ID")
    private String sourceId;
    @ApiModelProperty("原单Code")
    private String sourceCode;
    @ApiModelProperty("原单类型")
    private Integer sourceBillType;
    @TableField(exist=false)
    private String sourceBillTypeName;
    @ApiModelProperty("单据类型")
    private Integer billType;
    @ApiModelProperty("订单状态")
    private Integer billStatus;
    @ApiModelProperty("订单状态")
    @TableField(exist=false)
    private String billStatusName;
    @ApiModelProperty("单据时间")
    private Date billDate;
    @ApiModelProperty("发货方式")
    private String cdiDefaultType;
    @ApiModelProperty("发货方式")
    @TableField(exist=false)
    private String cdiDefaultTypeName;
    @ApiModelProperty("发货地址")
    private String cdiDeliveryAddress;
    @ApiModelProperty("物流公司")
    private String cdiLogisticsId;
    @ApiModelProperty("物流公司")
    @TableField(exist=false)
    private String cdiLogistics;
    @ApiModelProperty("物流单号")
    private String cdiLogisticsNo;
    @ApiModelProperty("收件人联系电话")
    private String cdiRecipientsPhone;
    @ApiModelProperty("收件人")
    private String cdiRecipients;
    @ApiModelProperty("省")
    private String cdiProvince;
    @ApiModelProperty("市")
    private String cdiCity;
    @ApiModelProperty("区、县")
    private String cdiDistrict;
    @ApiModelProperty("详细地址")
    private String cdiAddress;
    @ApiModelProperty("邮政编码")
    private String postCode;
    @ApiModelProperty("件数")
    private BigDecimal number;
    @ApiModelProperty("总重量")
    private BigDecimal totalWeight;
    @ApiModelProperty("保价")
    private BigDecimal insurance;
    @ApiModelProperty("费用总计")
    private BigDecimal totalCharge;

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

    public Integer getSourceBillType() {
        return sourceBillType;
    }

    public void setSourceBillType(Integer sourceBillType) {
        this.sourceBillType = sourceBillType;
    }

    public String getSourceBillTypeName() {

        if (null != sourceBillType) {
            return EnumConvertUtils.getName(BillType.class, sourceBillType);
        }
        return sourceBillTypeName;
    }

    public void setSourceBillTypeName(String sourceBillTypeName) {
        this.sourceBillTypeName = sourceBillTypeName;
    }

    public Integer getBillType() {
        return billType;
    }

    public void setBillType(Integer billType) {
        this.billType = billType;
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
    public void setBillStatusName(String billStatusName) {
        this.billStatusName = billStatusName;
    }

    public String getBillStatusName() {
        if (null != billStatus) {
            return EnumConvertUtils.getName(BillStatus.class, billStatus);
        }
        return billStatusName;
    }

    public String getCdiDeliveryAddress() {
        return cdiDeliveryAddress;
    }

    public void setCdiDeliveryAddress(String cdiDeliveryAddress) {
        this.cdiDeliveryAddress = cdiDeliveryAddress;
    }

    public String getCdiLogisticsId() {
        return cdiLogisticsId;
    }

    public void setCdiLogisticsId(String cdiLogisticsId) {
        this.cdiLogisticsId = cdiLogisticsId;
    }

    public String getCdiLogistics() {
        return cdiLogistics;
    }

    public void setCdiLogistics(String cdiLogistics) {
        this.cdiLogistics = cdiLogistics;
    }

    public String getCdiLogisticsNo() {
        return cdiLogisticsNo;
    }

    public void setCdiLogisticsNo(String cdiLogisticsNo) {
        this.cdiLogisticsNo = cdiLogisticsNo;
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

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public BigDecimal getNumber() {
        return number;
    }

    public void setNumber(BigDecimal number) {
        this.number = number;
    }

    public BigDecimal getTotalWeight() {
        return totalWeight;
    }

    public void setTotalWeight(BigDecimal totalWeight) {
        this.totalWeight = totalWeight;
    }

    public BigDecimal getInsurance() {
        return insurance;
    }

    public void setInsurance(BigDecimal insurance) {
        this.insurance = insurance;
    }

    public BigDecimal getTotalCharge() {
        return totalCharge;
    }

    public void setTotalCharge(BigDecimal totalCharge) {
        this.totalCharge = totalCharge;
    }

    public String getCdiDefaultType() {
        return cdiDefaultType;
    }

    public void setCdiDefaultType(String cdiDefaultType) {
        this.cdiDefaultType = cdiDefaultType;
    }

    public String getCdiDefaultTypeName() {
        return cdiDefaultTypeName;
    }

    public void setCdiDefaultTypeName(String cdiDefaultTypeName) {
        this.cdiDefaultTypeName = cdiDefaultTypeName;
    }
}
