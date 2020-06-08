package org.jeecg.modules.report.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecg.modules.basic.entity.CBasicEntity;

@Data
@TableName("sl_report")
@ApiModel(value = "Report", description = "报表")
public class Report extends CBasicEntity {


    @ApiModelProperty("表名称")
    private String tableName;

    @ApiModelProperty("函数类型")
    private Integer functionTypeId;

    @ApiModelProperty("函数字段")
    private String column;

    @ApiModelProperty("聚合字段")
    private String aggColumn;

    @ApiModelProperty("条件")
    private String condition;

}
