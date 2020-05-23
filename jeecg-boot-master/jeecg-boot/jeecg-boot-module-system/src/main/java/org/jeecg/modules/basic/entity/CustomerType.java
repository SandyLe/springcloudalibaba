package org.jeecg.modules.basic.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@TableName("sl_customer_type")
@ApiModel(value = "CustomerType", description = "客户类型")
public class CustomerType extends CBasicEntity {

    @ApiModelProperty("价格状态")
    private String priceFlag;

    public String getPriceFlag() {
        return priceFlag;
    }

    public void setPriceFlag(String priceFlag) {
        this.priceFlag = priceFlag;
    }
}
