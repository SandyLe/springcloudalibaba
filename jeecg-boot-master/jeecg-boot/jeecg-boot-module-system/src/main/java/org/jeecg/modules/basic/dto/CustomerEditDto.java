package org.jeecg.modules.basic.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

public class CustomerEditDto {
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
    @ApiModelProperty("名称")
    private String name;
    @ApiModelProperty("代码")
    private String code;
    @ApiModelProperty("数据状态")
    private Integer rowSts;
    @ApiModelProperty("排序")
    private Integer sort;
    @ApiModelProperty("备注")
    private String content;
    @ApiModelProperty("客户类型ID")
    private String customerTypeId;
    @ApiModelProperty("客户类型")
    @TableField(exist=false)
    private String customerType;
    @ApiModelProperty("手机")
    private String phone;
    @ApiModelProperty("微信号")
    private String wechatId;
    @ApiModelProperty("微信昵称")
    private String wechatNickName;
    @ApiModelProperty("客户来源ID")
    private String customerSourceId;
    @ApiModelProperty("客户来源")
    @TableField(exist=false)
    private String customerSource;
    @ApiModelProperty("微信会员")
    private String memberNo;
    @ApiModelProperty("会员手机")
    private String memberPhone;
    @ApiModelProperty("会员昵称")
    private String memberNickName;
    @ApiModelProperty("客户昵称")
    private String nickName;
    @ApiModelProperty("性别")
    private String gender;
    @ApiModelProperty("生日")
    private String birthday;
    @ApiModelProperty("联系人")
    private String linkman;
    @ApiModelProperty("电话")
    private String tel;
    @ApiModelProperty("邮箱")
    private String email;
    @ApiModelProperty("传真")
    private String fax;
    @ApiModelProperty("银行账号")
    private String bankaccount;
    @ApiModelProperty("银行账户")
    private String bankacctName;
    @ApiModelProperty("银行名称")
    private String bankName;
    @ApiModelProperty("默认折扣")
    private BigDecimal discount;
    @ApiModelProperty("折扣类型")
    private String discountTypeId;
    @ApiModelProperty("省")
    private String province;
    @ApiModelProperty("市")
    private String city;
    @ApiModelProperty("区")
    private String district;
    @ApiModelProperty("详细地址")
    private String address;
    @ApiModelProperty("开票信息")
    private String billingInfo;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getRowSts() {
        return rowSts;
    }

    public void setRowSts(Integer rowSts) {
        this.rowSts = rowSts;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCustomerTypeId() {
        return customerTypeId;
    }

    public void setCustomerTypeId(String customerTypeId) {
        this.customerTypeId = customerTypeId;
    }

    public String getCustomerType() {
        return customerType;
    }

    public void setCustomerType(String customerType) {
        this.customerType = customerType;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getWechatId() {
        return wechatId;
    }

    public void setWechatId(String wechatId) {
        this.wechatId = wechatId;
    }

    public String getWechatNickName() {
        return wechatNickName;
    }

    public void setWechatNickName(String wechatNickName) {
        this.wechatNickName = wechatNickName;
    }

    public String getCustomerSourceId() {
        return customerSourceId;
    }

    public void setCustomerSourceId(String customerSourceId) {
        this.customerSourceId = customerSourceId;
    }

    public String getCustomerSource() {
        return customerSource;
    }

    public void setCustomerSource(String customerSource) {
        this.customerSource = customerSource;
    }

    public String getMemberNo() {
        return memberNo;
    }

    public void setMemberNo(String memberNo) {
        this.memberNo = memberNo;
    }

    public String getMemberPhone() {
        return memberPhone;
    }

    public void setMemberPhone(String memberPhone) {
        this.memberPhone = memberPhone;
    }

    public String getMemberNickName() {
        return memberNickName;
    }

    public void setMemberNickName(String memberNickName) {
        this.memberNickName = memberNickName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getLinkman() {
        return linkman;
    }

    public void setLinkman(String linkman) {
        this.linkman = linkman;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getBankaccount() {
        return bankaccount;
    }

    public void setBankaccount(String bankaccount) {
        this.bankaccount = bankaccount;
    }

    public String getBankacctName() {
        return bankacctName;
    }

    public void setBankacctName(String bankacctName) {
        this.bankacctName = bankacctName;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public String getDiscountTypeId() {
        return discountTypeId;
    }

    public void setDiscountTypeId(String discountTypeId) {
        this.discountTypeId = discountTypeId;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBillingInfo() {
        return billingInfo;
    }

    public void setBillingInfo(String billingInfo) {
        this.billingInfo = billingInfo;
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
