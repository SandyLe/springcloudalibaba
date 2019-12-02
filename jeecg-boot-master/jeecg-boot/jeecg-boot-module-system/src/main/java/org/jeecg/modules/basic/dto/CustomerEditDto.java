package org.jeecg.modules.basic.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.Date;

public class CustomerEditDto {
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
    private Integer gender;
    @ApiModelProperty("生日")
    private Date birthday;
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


}
