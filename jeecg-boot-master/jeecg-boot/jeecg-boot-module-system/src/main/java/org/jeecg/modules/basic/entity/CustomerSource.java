package org.jeecg.modules.basic.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;

@TableName("sl_customer_source")
@ApiModel(value = "CustomerSource", description = "客户来源")
public class CustomerSource extends CBasicEntity {
}
