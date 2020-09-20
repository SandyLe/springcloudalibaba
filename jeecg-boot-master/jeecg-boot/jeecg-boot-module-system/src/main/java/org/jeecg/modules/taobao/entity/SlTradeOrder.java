package org.jeecg.modules.taobao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecg.modules.basic.entity.BaseBill;

@Data
@TableName("sl_trade_order")
@ApiModel(value = "SlTradeOrder", description = "交易")
public class SlTradeOrder extends BaseBill {

    @ApiModelProperty("手工调整金额.格式为:1.01;单位:元;精确到小数点后两位")
    private String adjustFee;
    @ApiModelProperty("买家是否已评价。可选值：true(已评价)，false(未评价)")
    private Boolean buyerRate;
    @ApiModelProperty("交易商品对应的类目ID")
    private Long cid;
    @ApiModelProperty("最晚揽收时间")
    private String collectTime;
    @ApiModelProperty("物流截单时间，分钟")
    private String cutoffMinutes;
    @ApiModelProperty("最晚发货时间")
    private String deliveryTime;
    @ApiModelProperty("子订单级订单优惠金额。精确到2位小数;单位:元。如:200.07，表示:200元7分")
    private String discountFee;
    @ApiModelProperty("物流时效，相对时间，单位是天")
    private String esTime;
    @ApiModelProperty("表示订单交易是否含有对应的代销采购单。如果该订单中存在一个对应的代销采购单，那么该值为true；反之，该值为false。")
    private Boolean isDaixiao;
    @ApiModelProperty("购买数量。取值范围:大于零的整数")
    private Long num;
    @ApiModelProperty("商品数字编号")
    private Long numIid;
    @ApiModelProperty("子订单编号")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long oid;
    @ApiModelProperty("oidStr")
    private String oidStr;
    @ApiModelProperty("子订单来源,如jhs(聚划算)、taobao(淘宝)、wap(无线)")
    private String orderFrom;
    @ApiModelProperty("outer_sku_id")
    private String outerSkuId;
    @ApiModelProperty("优惠分摊")
    private String partMjzDiscount;
    @ApiModelProperty("子订单实付金额")
    private String payment;
    @ApiModelProperty("商品图片的绝对路径")
    private String picPath;
    @ApiModelProperty("商品价格")
    private String price;
    @ApiModelProperty("时效服务字段，服务字段，会有多个服务值，以英文半角逗号,切割")
    private String promiseService;
    @ApiModelProperty("使用淘金币的数量，以分为单位，和订单标propoint中间那一段一样")
    private String propoint;
    @ApiModelProperty("退款状态")
    private String refundStatus;
    @ApiModelProperty("卖家是否已评价。可选值：true(已评价)，false(未评价)")
    private Boolean sellerRate;
    @ApiModelProperty("卖家类型，可选值为：B（商城商家），C（普通卖家）")
    private String sellerType;
    @ApiModelProperty("最晚签收时间")
    private String signTime;
    @ApiModelProperty("商品的最小库存单位Sku的id.可以通过taobao.item.sku.get获取详细的Sku信息")
    private String skuId;
    @ApiModelProperty("SKU的值。如：机身颜色:黑色;手机套餐:官方标配")
    private String skuPropertiesName;
    @ApiModelProperty("订单状态")
    private String status;
    @ApiModelProperty("发货的仓库编码")
    private String storeCode;
    @ApiModelProperty("时效服务身份，如tmallPromise代表天猫时效承诺")
    private String timingPromise;
    @ApiModelProperty("商品标题")
    private String title;
    @ApiModelProperty("应付金额")
    private String totalFee;

    public String getAdjustFee() {
        return adjustFee;
    }

    public void setAdjustFee(String adjustFee) {
        this.adjustFee = adjustFee;
    }

    public Boolean getBuyerRate() {
        return buyerRate;
    }

    public void setBuyerRate(Boolean buyerRate) {
        this.buyerRate = buyerRate;
    }

    public Long getCid() {
        return cid;
    }

    public void setCid(Long cid) {
        this.cid = cid;
    }

    public String getCollectTime() {
        return collectTime;
    }

    public void setCollectTime(String collectTime) {
        this.collectTime = collectTime;
    }

    public String getCutoffMinutes() {
        return cutoffMinutes;
    }

    public void setCutoffMinutes(String cutoffMinutes) {
        this.cutoffMinutes = cutoffMinutes;
    }

    public String getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(String deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public String getDiscountFee() {
        return discountFee;
    }

    public void setDiscountFee(String discountFee) {
        this.discountFee = discountFee;
    }

    public String getEsTime() {
        return esTime;
    }

    public void setEsTime(String esTime) {
        this.esTime = esTime;
    }

    public Boolean getDaixiao() {
        return isDaixiao;
    }

    public void setDaixiao(Boolean daixiao) {
        isDaixiao = daixiao;
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

    public Long getOid() {
        return oid;
    }

    public void setOid(Long oid) {
        this.oid = oid;
    }

    public String getOidStr() {
        return oidStr;
    }

    public void setOidStr(String oidStr) {
        this.oidStr = oidStr;
    }

    public String getOrderFrom() {
        return orderFrom;
    }

    public void setOrderFrom(String orderFrom) {
        this.orderFrom = orderFrom;
    }

    public String getOuterSkuId() {
        return outerSkuId;
    }

    public void setOuterSkuId(String outerSkuId) {
        this.outerSkuId = outerSkuId;
    }

    public String getPartMjzDiscount() {
        return partMjzDiscount;
    }

    public void setPartMjzDiscount(String partMjzDiscount) {
        this.partMjzDiscount = partMjzDiscount;
    }

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }

    public String getPicPath() {
        return picPath;
    }

    public void setPicPath(String picPath) {
        this.picPath = picPath;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPromiseService() {
        return promiseService;
    }

    public void setPromiseService(String promiseService) {
        this.promiseService = promiseService;
    }

    public String getPropoint() {
        return propoint;
    }

    public void setPropoint(String propoint) {
        this.propoint = propoint;
    }

    public String getRefundStatus() {
        return refundStatus;
    }

    public void setRefundStatus(String refundStatus) {
        this.refundStatus = refundStatus;
    }

    public Boolean getSellerRate() {
        return sellerRate;
    }

    public void setSellerRate(Boolean sellerRate) {
        this.sellerRate = sellerRate;
    }

    public String getSellerType() {
        return sellerType;
    }

    public void setSellerType(String sellerType) {
        this.sellerType = sellerType;
    }

    public String getSignTime() {
        return signTime;
    }

    public void setSignTime(String signTime) {
        this.signTime = signTime;
    }

    public String getSkuId() {
        return skuId;
    }

    public void setSkuId(String skuId) {
        this.skuId = skuId;
    }

    public String getSkuPropertiesName() {
        return skuPropertiesName;
    }

    public void setSkuPropertiesName(String skuPropertiesName) {
        this.skuPropertiesName = skuPropertiesName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStoreCode() {
        return storeCode;
    }

    public void setStoreCode(String storeCode) {
        this.storeCode = storeCode;
    }

    public String getTimingPromise() {
        return timingPromise;
    }

    public void setTimingPromise(String timingPromise) {
        this.timingPromise = timingPromise;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(String totalFee) {
        this.totalFee = totalFee;
    }
}
