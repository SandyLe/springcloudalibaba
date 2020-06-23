package org.jeecg.modules.report.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecg.modules.basic.entity.CBasicEntity;

@Data
@TableName("sl_report")
@ApiModel(value = "Report", description = "报表")
public class Report extends CBasicEntity {

    //form xxx
    @ApiModelProperty("表名称")
    private String tableName;

    // sum、avg、count
    @ApiModelProperty("函数类型")
    private Integer functionTypeId;
    @ApiModelProperty("函数类型")
    @TableField(exist = false)
    private String functionTypeIdName;

    // sum(moeny), count(id)
    @ApiModelProperty("函数字段")
    private String functionColumn;

    // group by id
    @ApiModelProperty("聚合字段")
    private String aggColumn;

    // id =123 and name='tom'
    @ApiModelProperty("条件")
    private String condition;

    // order by id
    @ApiModelProperty("排序列")
    private String orderColumn;

    // dashboard、produt、order
    @ApiModelProperty("归属页面")
    private Integer pageType;
    @ApiModelProperty("归属页面")
    @TableField(exist = false)
    private String pageTypeName;

    // 文本、条形图、柱状图
    @ApiModelProperty("展示方式")
    private Integer showType;

    // limit 5
    @ApiModelProperty("展示行数")
    private Integer limitNum;
}
