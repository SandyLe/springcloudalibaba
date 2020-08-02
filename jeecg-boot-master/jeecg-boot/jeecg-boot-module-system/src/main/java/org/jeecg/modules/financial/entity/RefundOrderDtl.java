package org.jeecg.modules.financial.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecg.modules.basic.entity.CBasicEntity;

import java.math.BigDecimal;
import java.util.Date;

@ApiModel(value = "ReceiptOrderDtl", description = "退款单退款明细")
@Data
@TableName("sl_refund_order_dtl")
public class RefundOrderDtl extends CBasicEntity {

    @ApiModelProperty("原单ID")
    private String sourceId;
    @ApiModelProperty("原单编号")
    private String sourceCode;
    @ApiModelProperty("原单类型")
    private Integer sourceBillType;
    @ApiModelProperty("原单类型名称")
    @TableField(exist=false)
    private String sourceBillTypeName;
    @ApiModelProperty("销售订单ID")
    private String sourceBillId;
    @ApiModelProperty("原单Code")
    private String sourceBillCode;
    @ApiModelProperty("付款金额")
    private BigDecimal payAmount;
    @ApiModelProperty("付款时间")
    private Date payDate;
    @ApiModelProperty("付款方式")
    private Integer payType;
    @ApiModelProperty("订单状态")
    @TableField(exist=false)
    private String payTypeName;
    @ApiModelProperty("费用ID")
    private String expenseId;
    @ApiModelProperty("费用名称")
    @TableField(exist=false)
    private String expenseName;
    @ApiModelProperty("退款人ID")
    private String receiverId;
    @ApiModelProperty("退款人")
    @TableField(exist=false)
    private String receiverName;

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public Integer getSourceBillType() {
        return sourceBillType;
    }

    public String getSourceCode() {
        return sourceCode;
    }

    public void setSourceCode(String sourceCode) {
        this.sourceCode = sourceCode;
    }

    public void setSourceBillType(Integer sourceBillType) {
        this.sourceBillType = sourceBillType;
    }

    public String getSourceBillTypeName() {
        return sourceBillTypeName;
    }

    public void setSourceBillTypeName(String sourceBillTypeName) {
        this.sourceBillTypeName = sourceBillTypeName;
    }

    public String getSourceBillId() {
        return sourceBillId;
    }

    public void setSourceBillId(String sourceBillId) {
        this.sourceBillId = sourceBillId;
    }

    public String getSourceBillCode() {
        return sourceBillCode;
    }

    public void setSourceBillCode(String sourceBillCode) {
        this.sourceBillCode = sourceBillCode;
    }

    public BigDecimal getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(BigDecimal payAmount) {
        this.payAmount = payAmount;
    }

    public Date getPayDate() {
        return payDate;
    }

    public void setPayDate(Date payDate) {
        this.payDate = payDate;
    }

    public Integer getPayType() {
        return payType;
    }

    public void setPayType(Integer payType) {
        this.payType = payType;
    }

    public String getPayTypeName() {
        return payTypeName;
    }

    public void setPayTypeName(String payTypeName) {
        this.payTypeName = payTypeName;
    }

    public String getExpenseId() {
        return expenseId;
    }

    public void setExpenseId(String expenseId) {
        this.expenseId = expenseId;
    }

    public String getExpenseName() {
        return expenseName;
    }

    public void setExpenseName(String expenseName) {
        this.expenseName = expenseName;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }
}
