package org.jeecg.modules.taobao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecg.modules.basic.entity.BaseBill;

import java.util.Date;

@Data
@TableName("sl_print_order_assignment")
@ApiModel(value = "PrintOrderAssignment", description = "交易")
public class PrintOrderAssignment extends BaseBill {

    @ApiModelProperty("交易编号")
    private String tid;
    @ApiModelProperty("商品购买数量")
    private Long num;
    @ApiModelProperty("商品数字编号")
    private Long numIid;

    @ApiModelProperty("收货人的姓名")
    private String receiverName;
    @ApiModelProperty("收货人国籍")
    private String receiverCountry;
    @ApiModelProperty("收货人的所在省份")
    private String receiverState;
    @ApiModelProperty("收货人的所在城市")
    private String receiverCity;
    @ApiModelProperty("收货人的所在地区")
    private String receiverDistrict;
    @ApiModelProperty("收货人街道地址")
    private String receiverTown;
    @ApiModelProperty("收货人的详细地址")
    private String receiverAddress;
    @ApiModelProperty("收货人的邮编")
    private String receiverZip;
    @ApiModelProperty("收货人的手机号码")
    private String receiverMobile;
    @ApiModelProperty("收货人的电话号码")
    private String receiverPhone;

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public Long getNum() {
        return num;
    }

    public void setNum(Long num) {
        this.num = num;
    }

    public Long getNumIid() {
        return numIid;
    }

    public void setNumIid(Long numIid) {
        this.numIid = numIid;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getReceiverCountry() {
        return receiverCountry;
    }

    public void setReceiverCountry(String receiverCountry) {
        this.receiverCountry = receiverCountry;
    }

    public String getReceiverState() {
        return receiverState;
    }

    public void setReceiverState(String receiverState) {
        this.receiverState = receiverState;
    }

    public String getReceiverCity() {
        return receiverCity;
    }

    public void setReceiverCity(String receiverCity) {
        this.receiverCity = receiverCity;
    }

    public String getReceiverDistrict() {
        return receiverDistrict;
    }

    public void setReceiverDistrict(String receiverDistrict) {
        this.receiverDistrict = receiverDistrict;
    }

    public String getReceiverTown() {
        return receiverTown;
    }

    public void setReceiverTown(String receiverTown) {
        this.receiverTown = receiverTown;
    }

    public String getReceiverAddress() {
        return receiverAddress;
    }

    public void setReceiverAddress(String receiverAddress) {
        this.receiverAddress = receiverAddress;
    }

    public String getReceiverZip() {
        return receiverZip;
    }

    public void setReceiverZip(String receiverZip) {
        this.receiverZip = receiverZip;
    }

    public String getReceiverMobile() {
        return receiverMobile;
    }

    public void setReceiverMobile(String receiverMobile) {
        this.receiverMobile = receiverMobile;
    }

    public String getReceiverPhone() {
        return receiverPhone;
    }

    public void setReceiverPhone(String receiverPhone) {
        this.receiverPhone = receiverPhone;
    }
}
