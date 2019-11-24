package org.jeecg.modules.basic.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;

@TableName("sl_customer_type")
public class CustomerType extends BasicEntity {

    @ApiModelProperty("价格状态")
    private String priceFlag;

    public String getPriceFlag() {
        return priceFlag;
    }

    public void setPriceFlag(String priceFlag) {
        this.priceFlag = priceFlag;
    }
}
