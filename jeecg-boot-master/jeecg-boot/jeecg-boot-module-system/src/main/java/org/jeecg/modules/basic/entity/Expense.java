package org.jeecg.modules.basic.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;

@TableName("sl_expense")
@ApiModel(value = "Expense", description = "费用名称")
public class Expense extends BasicEntity {
}
