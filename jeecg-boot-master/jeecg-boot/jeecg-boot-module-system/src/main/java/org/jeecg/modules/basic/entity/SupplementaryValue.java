package org.jeecg.modules.basic.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@TableName("sl_supplementary_value")
@ApiModel(value = "SupplementaryValue", description = "辅助属性值")
public class SupplementaryValue extends CBasicEntity {

    @ApiModelProperty("所属对象")
    private String sourceId;

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }
}
