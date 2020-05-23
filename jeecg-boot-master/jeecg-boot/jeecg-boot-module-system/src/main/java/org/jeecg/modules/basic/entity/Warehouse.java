package org.jeecg.modules.basic.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@TableName("sl_warehouse")
@ApiModel(value = "Warehouse", description = "仓库")
public class Warehouse extends CBasicEntity {

    @ApiModelProperty("负责人ID")
    private String principalId;
    @ApiModelProperty("负责人名称")
    @TableField(exist=false)
    private String principalName;
    @ApiModelProperty("所属门店ID")
    private String belongsToId;
    @ApiModelProperty("所属门店名称")
    @TableField(exist=false)
    private String belongsToName;

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

    public String getPrincipalName() {
        return principalName;
    }

    public void setPrincipalName(String principalName) {
        this.principalName = principalName;
    }

    public String getBelongsToName() {
        return belongsToName;
    }

    public void setBelongsToName(String belongsToName) {
        this.belongsToName = belongsToName;
    }
}
