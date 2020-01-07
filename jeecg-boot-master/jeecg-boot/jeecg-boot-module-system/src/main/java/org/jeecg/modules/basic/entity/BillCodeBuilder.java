package org.jeecg.modules.basic.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@TableName("sl_bill_ode_builder")
@ApiModel(value = "BillCodeBuilder", description = "编号规则")
public class BillCodeBuilder extends BasicEntity {

    @ApiModelProperty("单据类型")
    private Integer billType;

    public Integer getBillType() {
        return billType;
    }

    public void setBillType(Integer billType) {
        this.billType = billType;
    }
}
