package org.jeecg.modules.taobao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecg.modules.basic.entity.BaseBill;

import java.util.Date;

@Data
@TableName("sl_trade")
@ApiModel(value = "Trade", description = "交易")
public class SlTrade extends BaseBill {

    @ApiModelProperty("交易编号 (父订单的交易编号)")
    private Long tid;
    @ApiModelProperty("卖家昵称")
    private String sellerNick;
    @ApiModelProperty("商品图片绝对途径")
    private String picPath;
    @ApiModelProperty("实付金额")
    private String payment;
    @ApiModelProperty("卖家是否已评价。可选值:true(已评价),false(未评价)")
    private Boolean sellerRate;
    @ApiModelProperty("邮费")
    private String postFee;
    @ApiModelProperty("交易状态")
    private String status;
    @ApiModelProperty("门店自提，总店发货，分店取货的门店自提订单标识")
    private String shopPick;
    @ApiModelProperty("商品购买数量")
    private Long num;
    @ApiModelProperty("商品数字编号")
    private Long numIid;
    @ApiModelProperty("交易标题，以店铺名作为此标题的值")
    private String title;
    @ApiModelProperty("交易类型列表，同时查询多种交易类型可用逗号分隔")
    private String type;
    @ApiModelProperty("商品价格")
    private String price;
    @ApiModelProperty("优惠系统优惠金额")
    private String discountFee;
    @ApiModelProperty("商品金额")
    private String totalFee;
    @ApiModelProperty("交易创建时间")
    private Date created;
    @ApiModelProperty("付款时间")
    private Date payTime;
    @ApiModelProperty("交易修改时间")
    private Date modified;
    @ApiModelProperty("交易结束时间")
    private Date endTime;
    @ApiModelProperty("卖家备注旗帜（与淘宝网上订单的卖家备注旗帜对应，只有卖家才能查看该字段）红、黄、绿、蓝、紫 分别对应 1、2、3、4、5")
    private Long sellerFlag;
    @ApiModelProperty("买家昵称")
    private String buyerNick;
    @ApiModelProperty("卖家实际收到的支付宝打款金额")
    private String receivedPayment;
    @ApiModelProperty("签收时间")
    private String signTime;
    @ApiModelProperty("派送时间")
    private String dispatchTime;
    @ApiModelProperty("揽收时间")
    private String collectTime;
    @ApiModelProperty("发货时间")
    private String deliveryTime;
    @ApiModelProperty("截单时间")
    private String cutoffMinutes;
    @ApiModelProperty("时间")
    private String osDate;
    @ApiModelProperty("时间段")
    private String osRange;
    @ApiModelProperty("时间")
    private String esDate;
    @ApiModelProperty("时间段")
    private String esRange;
    @ApiModelProperty("买家是否已评价。可选值:true(已评价),false(未评价)。如买家只评价未打分，此字段仍返回false")
    private Boolean buyerRate;
    @ApiModelProperty("交易内部来源")
    private String tradeFrom;
    @ApiModelProperty("卖家手工调整金额，精确到2位小数")
    private String adjustFee;
    @ApiModelProperty("创建交易时的物流方式（交易完成前，物流方式有可能改变，但系统里的这个字段一直不变）。可选值：free(卖家包邮),post(平邮),express(快递),ems(EMS),virtual(虚拟发货)，25(次日必达)，26(预约配送)")
    private String shippingType;
    @ApiModelProperty("订单出现异常问题的时候，给予用户的描述,没有异常的时候，此值为空")
    private String markDesc;
    @ApiModelProperty("使用信用卡支付金额数")
    private String creditCardFee;
    @ApiModelProperty("判断订单是否有买家留言，有买家留言返回true，否则返回false")
    private Boolean hasBuyerMessage;

    public String getSignTime() {
        return signTime;
    }

    public void setSignTime(String signTime) {
        this.signTime = signTime;
    }

    public String getDispatchTime() {
        return dispatchTime;
    }

    public void setDispatchTime(String dispatchTime) {
        this.dispatchTime = dispatchTime;
    }

    public String getCollectTime() {
        return collectTime;
    }

    public void setCollectTime(String collectTime) {
        this.collectTime = collectTime;
    }

    public String getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(String deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public String getCutoffMinutes() {
        return cutoffMinutes;
    }

    public void setCutoffMinutes(String cutoffMinutes) {
        this.cutoffMinutes = cutoffMinutes;
    }

    public String getOsDate() {
        return osDate;
    }

    public void setOsDate(String osDate) {
        this.osDate = osDate;
    }

    public String getOsRange() {
        return osRange;
    }

    public void setOsRange(String osRange) {
        this.osRange = osRange;
    }

    public String getEsDate() {
        return esDate;
    }

    public void setEsDate(String esDate) {
        this.esDate = esDate;
    }

    public String getEsRange() {
        return esRange;
    }

    public void setEsRange(String esRange) {
        this.esRange = esRange;
    }

    public Boolean getBuyerRate() {
        return buyerRate;
    }

    public void setBuyerRate(Boolean buyerRate) {
        this.buyerRate = buyerRate;
    }

    public String getTradeFrom() {
        return tradeFrom;
    }

    public void setTradeFrom(String tradeFrom) {
        this.tradeFrom = tradeFrom;
    }

    public String getAdjustFee() {
        return adjustFee;
    }

    public void setAdjustFee(String adjustFee) {
        this.adjustFee = adjustFee;
    }

    public String getShippingType() {
        return shippingType;
    }

    public void setShippingType(String shippingType) {
        this.shippingType = shippingType;
    }

    public String getMarkDesc() {
        return markDesc;
    }

    public void setMarkDesc(String markDesc) {
        this.markDesc = markDesc;
    }

    public String getCreditCardFee() {
        return creditCardFee;
    }

    public void setCreditCardFee(String creditCardFee) {
        this.creditCardFee = creditCardFee;
    }

    public Boolean getHasBuyerMessage() {
        return hasBuyerMessage;
    }

    public void setHasBuyerMessage(Boolean hasBuyerMessage) {
        this.hasBuyerMessage = hasBuyerMessage;
    }

    public Long getTid() {
        return tid;
    }

    public void setTid(Long tid) {
        this.tid = tid;
    }

    public String getSellerNick() {
        return sellerNick;
    }

    public void setSellerNick(String sellerNick) {
        this.sellerNick = sellerNick;
    }

    public String getPicPath() {
        return picPath;
    }

    public void setPicPath(String picPath) {
        this.picPath = picPath;
    }

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }

    public Boolean getSellerRate() {
        return sellerRate;
    }

    public void setSellerRate(Boolean sellerRate) {
        this.sellerRate = sellerRate;
    }

    public String getPostFee() {
        return postFee;
    }

    public void setPostFee(String postFee) {
        this.postFee = postFee;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getShopPick() {
        return shopPick;
    }

    public void setShopPick(String shopPick) {
        this.shopPick = shopPick;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDiscountFee() {
        return discountFee;
    }

    public void setDiscountFee(String discountFee) {
        this.discountFee = discountFee;
    }

    public String getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(String totalFee) {
        this.totalFee = totalFee;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getPayTime() {
        return payTime;
    }

    public void setPayTime(Date payTime) {
        this.payTime = payTime;
    }

    public Date getModified() {
        return modified;
    }

    public void setModified(Date modified) {
        this.modified = modified;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Long getSellerFlag() {
        return sellerFlag;
    }

    public void setSellerFlag(Long sellerFlag) {
        this.sellerFlag = sellerFlag;
    }

    public String getBuyerNick() {
        return buyerNick;
    }

    public void setBuyerNick(String buyerNick) {
        this.buyerNick = buyerNick;
    }

    public String getReceivedPayment() {
        return receivedPayment;
    }

    public void setReceivedPayment(String receivedPayment) {
        this.receivedPayment = receivedPayment;
    }
}
