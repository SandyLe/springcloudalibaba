package org.jeecg.modules.basic.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.jeecg.common.system.base.entity.JeecgEntity;

@ApiModel(value = "BasicEntity", description = "基础数据基类")
public class BasicEntity extends JeecgEntity {

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("代码")
    private String code;

    @ApiModelProperty("数据状态")
    private Integer rowSts;

    @ApiModelProperty("备注")
    private String content;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getRowSts() {
        return rowSts;
    }

    public void setRowSts(Integer rowSts) {
        this.rowSts = rowSts;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
