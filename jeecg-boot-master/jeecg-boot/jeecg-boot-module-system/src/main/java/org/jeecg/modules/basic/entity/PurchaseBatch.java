package org.jeecg.modules.basic.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;

@TableName("sl_purchase_batch")
@ApiModel(value = "PurchaseBatch", description = "采购批次")
public class PurchaseBatch extends CBasicEntity {
}
