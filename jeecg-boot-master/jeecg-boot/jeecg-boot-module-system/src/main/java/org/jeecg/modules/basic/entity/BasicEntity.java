package org.jeecg.modules.basic.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.jeecg.common.system.base.entity.JeecgEntity;
import org.jeecg.modules.basic.enums.EnumConvertUtils;
import org.jeecg.modules.basic.enums.RowSts;

@ApiModel(value = "BasicEntity", description = "基础数据基类")
public class BasicEntity extends JeecgEntity {

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("代码")
    private String code;

    @ApiModelProperty("数据状态")
    private Integer rowSts;
    @TableField(exist=false)
    private String rowStsName;

    @ApiModelProperty("排序")
    private Integer sort;

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

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public String getRowStsName() {
        if (null != this.rowSts) {
            return EnumConvertUtils.getName(RowSts.class, rowSts);
        }
        return null;
    }

    public void setRowStsName(String rowStsName) {
        this.rowStsName = rowStsName;
    }
}
