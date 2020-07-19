package org.jeecg.modules.report.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecg.modules.basic.entity.BasicEntity;
import org.jeecg.modules.basic.entity.CBasicEntity;

@Data
@TableName("sl_report")
@ApiModel(value = "Report", description = "报表")
public class Report extends BasicEntity {

    @ApiModelProperty("查询sql语句")
    private String querySql;

    // dashboard、produt、order
    @ApiModelProperty("归属页面")
    private String pageType;


    // 文本、条形图、柱状图
    @ApiModelProperty("展示方式")
    private String showType;

    @ApiModelProperty("接收查询结果的模型")
    private String resultBean;
}
