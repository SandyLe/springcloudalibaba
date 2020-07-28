package org.jeecg.modules.saleorder.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.jeecg.modules.basic.entity.CBasicEntity;

/**
 * @Description:
 * @Author: tomkluas
 * @Date: 2020/7/28 17:37
 * @Version: V1.0
 */
@TableName("sl_sale_order_channel")
@ApiModel(value = "SaleOrderChannel", description = "销售渠道")
public class SaleOrderChannel extends CBasicEntity {
    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }
    @ApiModelProperty("父渠道ID")
    private int parentId;

}
