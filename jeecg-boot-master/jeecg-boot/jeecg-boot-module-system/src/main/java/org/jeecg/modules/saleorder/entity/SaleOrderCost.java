package org.jeecg.modules.saleorder.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecg.modules.basic.entity.CBasicEntity;

import java.math.BigDecimal;

@ApiModel(value = "SaleOrderCost", description = "销售成本")
@Data
@TableName("sl_sale_order_cost")
public class SaleOrderCost extends CBasicEntity {

    @ApiModelProperty("销售订单ID")
    private String sourceId;
    @ApiModelProperty("原单Code")
    private String sourceCode;
    @ApiModelProperty("收款方ID")
    private String payeeId;
    @ApiModelProperty("收款方")
    @TableField(exist=false)
    private String payee;
    @ApiModelProperty("金额")
    private BigDecimal amount;
    @ApiModelProperty("订单状态")
    private Integer billStatusId;
    @ApiModelProperty("订单状态")
    @TableField(exist=false)
    private Integer billStatusName;
    @ApiModelProperty("状态")
    @TableField(exist=false)
    private String billStatus;
    @ApiModelProperty("费用ID")
    private String expenseId;
    @ApiModelProperty("费用")
    @TableField(exist=false)
    private String expense;

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

    public String getPayeeId() {
        return payeeId;
    }

    public void setPayeeId(String payeeId) {
        this.payeeId = payeeId;
    }

    public String getPayee() {
        return payee;
    }

    public void setPayee(String payee) {
        this.payee = payee;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getExpenseId() {
        return expenseId;
    }

    public void setExpenseId(String expenseId) {
        this.expenseId = expenseId;
    }

    public String getExpense() {
        return expense;
    }

    public void setExpense(String expense) {
        this.expense = expense;
    }

    public Integer getBillStatusId() {
        return billStatusId;
    }

    public void setBillStatusId(Integer billStatusId) {
        this.billStatusId = billStatusId;
    }

    public String getBillStatus() {
        return billStatus;
    }

    public void setBillStatus(String billStatus) {
        this.billStatus = billStatus;
    }

    public Integer getBillStatusName() {
        return billStatusName;
    }

    public void setBillStatusName(Integer billStatusName) {
        this.billStatusName = billStatusName;
    }
}
