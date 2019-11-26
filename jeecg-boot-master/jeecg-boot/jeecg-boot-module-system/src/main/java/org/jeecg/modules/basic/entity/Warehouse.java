package org.jeecg.modules.basic.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@TableName("sl_warehouse")
@ApiModel(value = "Warehouse", description = "仓库")
public class Warehouse extends BasicEntity {

    @ApiModelProperty("负责人ID")
    private String principalId;

    @ApiModelProperty("所属门店ID")
    private String belongsToId;

    public String getPrincipalId() {
        return principalId;
    }

    public void setPrincipalId(String principalId) {
        this.principalId = principalId;
    }

    public String getBelongsToId() {
        return belongsToId;
    }

    public void setBelongsToId(String belongsToId) {
        this.belongsToId = belongsToId;
    }
}
