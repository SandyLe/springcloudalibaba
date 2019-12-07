package org.jeecg.modules.basic.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

public class CustomerEditDto {
    /** ID */
    @ApiModelProperty(value = "ID")
    private java.lang.String id;
    /** 创建人 */
    @ApiModelProperty(value = "创建人")
    private java.lang.String createBy;
    /** 创建时间 */
    @ApiModelProperty(value = "创建时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private java.util.Date createTime;
    /** 更新人 */
    @ApiModelProperty(value = "更新人")
    private java.lang.String updateBy;
    /** 更新时间 */
    @ApiModelProperty(value = "更新时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private java.util.Date updateTime;
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
    private java.lang.String cdi_id;
    /** 创建人 */
    @ApiModelProperty(value = "创建人")
    private java.lang.String cdi_createBy;
    /** 创建时间 */
    @ApiModelProperty(value = "创建时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private java.util.Date cdi_createTime;
    /** 更新人 */
    @ApiModelProperty(value = "更新人")
    private java.lang.String cdi_updateBy;
    /** 更新时间 */
    @ApiModelProperty(value = "更新时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private java.util.Date cdi_updateTime;
    @ApiModelProperty("名称")
    private String cdi_name;
    @ApiModelProperty("代码")
    private String cdi_code;
    @ApiModelProperty("数据状态")
    private Integer cdi_rowSts;
    @ApiModelProperty("排序")
    private Integer cdi_sort;
    @ApiModelProperty("备注")
    private String cdi_content;
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
    @ApiModelProperty("收件人联系电话")
    private String cdi_recipients_phone;
    @ApiModelProperty("收件人")
    private String cdi_recipients;
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

    public String getCdi_id() {
        return cdi_id;
    }

    public void setCdi_id(String cdi_id) {
        this.cdi_id = cdi_id;
    }

    public String getCdi_createBy() {
        return cdi_createBy;
    }

    public void setCdi_createBy(String cdi_createBy) {
        this.cdi_createBy = cdi_createBy;
    }

    public Date getCdi_createTime() {
        return cdi_createTime;
    }

    public void setCdi_createTime(Date cdi_createTime) {
        this.cdi_createTime = cdi_createTime;
    }

    public String getCdi_updateBy() {
        return cdi_updateBy;
    }

    public void setCdi_updateBy(String cdi_updateBy) {
        this.cdi_updateBy = cdi_updateBy;
    }

    public Date getCdi_updateTime() {
        return cdi_updateTime;
    }

    public void setCdi_updateTime(Date cdi_updateTime) {
        this.cdi_updateTime = cdi_updateTime;
    }

    public String getCdi_name() {
        return cdi_name;
    }

    public void setCdi_name(String cdi_name) {
        this.cdi_name = cdi_name;
    }

    public String getCdi_code() {
        return cdi_code;
    }

    public void setCdi_code(String cdi_code) {
        this.cdi_code = cdi_code;
    }

    public Integer getCdi_rowSts() {
        return cdi_rowSts;
    }

    public void setCdi_rowSts(Integer cdi_rowSts) {
        this.cdi_rowSts = cdi_rowSts;
    }

    public Integer getCdi_sort() {
        return cdi_sort;
    }

    public void setCdi_sort(Integer cdi_sort) {
        this.cdi_sort = cdi_sort;
    }

    public String getCdi_content() {
        return cdi_content;
    }

    public void setCdi_content(String cdi_content) {
        this.cdi_content = cdi_content;
    }

    public String getCdi_recipients() {
        return cdi_recipients;
    }

    public void setCdi_recipients(String cdi_recipients) {
        this.cdi_recipients = cdi_recipients;
    }
}
