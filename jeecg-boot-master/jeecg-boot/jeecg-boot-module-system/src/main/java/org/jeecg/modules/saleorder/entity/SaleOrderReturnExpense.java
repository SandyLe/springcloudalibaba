package org.jeecg.modules.saleorder.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecg.common.system.base.entity.JeecgEntity;

import java.math.BigDecimal;

@Data
@TableName("sl_sale_order_return_expense")
public class SaleOrderReturnExpense extends JeecgEntity {

    @ApiModelProperty("销售订单ID")
    private String sourceId;
    @ApiModelProperty("产品ID")
    private String expenseId;
    @ApiModelProperty("产品")
    @TableField(exist=false)
    private String expense;
    @ApiModelProperty("费用")
    private BigDecimal amount;

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
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

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
