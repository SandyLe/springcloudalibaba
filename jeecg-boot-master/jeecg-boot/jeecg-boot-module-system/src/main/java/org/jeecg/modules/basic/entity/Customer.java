package org.jeecg.modules.basic.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang.StringUtils;

import java.math.BigDecimal;
import java.util.Date;

@TableName("sl_customer")
@ApiModel(value = "Customer", description = "客户")
public class Customer extends BasicEntity {

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
    @TableField(exist=false)
    private String genderName;
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
    @TableField(exist=false)
    private String discountType;
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
    @TableField(exist=false)
    private String fullAddress;
    @TableField(exist=false)
    private String info;

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

    public String getGenderName() {
        return genderName;
    }

    public void setGenderName(String genderName) {
        this.genderName = genderName;
    }

    public String getDiscountType() {
        return discountType;
    }

    public void setDiscountType(String discountType) {
        this.discountType = discountType;
    }

    public String getFullAddress() {
        return fullAddress;
    }

    public void setFullAddress(String fullAddress) {
        this.fullAddress = fullAddress;
    }

    public String getInfo() {
        StringBuilder sb = new StringBuilder();
        if (StringUtils.isNotBlank(this.getName())) {
            sb.append(this.getName() + " | ");
        }
        if (StringUtils.isNotBlank(this.getCode())) {
            sb.append(this.getCode() + " | ");
        }
        if (StringUtils.isNotBlank(this.getPhone())) {
            sb.append(this.getPhone());
        }
        return sb.toString();
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
