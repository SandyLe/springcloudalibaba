package org.jeecg.modules.basic.dto;

import io.swagger.annotations.ApiModelProperty;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public class DeliveryEditDto {
    /** ID */
    @ApiModelProperty(value = "ID")
    private String id;
    /** 创建人 */
    @ApiModelProperty(value = "创建人")
    private String createBy;
    /** 创建时间 */
    @ApiModelProperty(value = "创建时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    /** 更新人 */
    @ApiModelProperty(value = "更新人")
    private String updateBy;
    /** 更新时间 */
    @ApiModelProperty(value = "更新时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
    @ApiModelProperty("出货仓库ID")
    private String warehouseId;
    @ApiModelProperty("发货时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date deliveryTime;
    @ApiModelProperty("安装时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date installTime;

    /** ID */
    @ApiModelProperty(value = "ID")
    private String cdiId;
    /** 创建人 */
    @ApiModelProperty(value = "创建人")
    private String cdiCreateBy;
    /** 创建时间 */
    @ApiModelProperty(value = "创建时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date cdiCreateTime;
    /** 更新人 */
    @ApiModelProperty(value = "更新人")
    private String cdiUpdateBy;
    /** 更新时间 */
    @ApiModelProperty(value = "更新时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date cdiUpdateTime;
    @ApiModelProperty("名称")
    private String cdiName;
    @ApiModelProperty("代码")
    private String cdiCode;
    @ApiModelProperty("数据状态")
    private Integer cdiRowSts;
    @ApiModelProperty("排序")
    private Integer cdiSort;
    @ApiModelProperty("备注")
    private String cdiContent;
    @ApiModelProperty("客户")
    private String cdiSourceId;
    @ApiModelProperty("发货方式")
    private String cdiDefaultType;
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
    @ApiModelProperty("物流公司")
    private String cdiLogistics;
    @ApiModelProperty("网点")
    private String cdiBranch;
    @ApiModelProperty("电话")
    private String cdiTel;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(String warehouseId) {
        this.warehouseId = warehouseId;
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

    public String getCdiId() {
        return cdiId;
    }

    public void setCdiId(String cdiId) {
        this.cdiId = cdiId;
    }

    public String getCdiCreateBy() {
        return cdiCreateBy;
    }

    public void setCdiCreateBy(String cdiCreateBy) {
        this.cdiCreateBy = cdiCreateBy;
    }

    public Date getCdiCreateTime() {
        return cdiCreateTime;
    }

    public void setCdiCreateTime(Date cdiCreateTime) {
        this.cdiCreateTime = cdiCreateTime;
    }

    public String getCdiUpdateBy() {
        return cdiUpdateBy;
    }

    public void setCdiUpdateBy(String cdiUpdateBy) {
        this.cdiUpdateBy = cdiUpdateBy;
    }

    public Date getCdiUpdateTime() {
        return cdiUpdateTime;
    }

    public void setCdiUpdateTime(Date cdiUpdateTime) {
        this.cdiUpdateTime = cdiUpdateTime;
    }

    public String getCdiName() {
        return cdiName;
    }

    public void setCdiName(String cdiName) {
        this.cdiName = cdiName;
    }

    public String getCdiCode() {
        return cdiCode;
    }

    public void setCdiCode(String cdiCode) {
        this.cdiCode = cdiCode;
    }

    public Integer getCdiRowSts() {
        return cdiRowSts;
    }

    public void setCdiRowSts(Integer cdiRowSts) {
        this.cdiRowSts = cdiRowSts;
    }

    public Integer getCdiSort() {
        return cdiSort;
    }

    public void setCdiSort(Integer cdiSort) {
        this.cdiSort = cdiSort;
    }

    public String getCdiContent() {
        return cdiContent;
    }

    public void setCdiContent(String cdiContent) {
        this.cdiContent = cdiContent;
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
}
