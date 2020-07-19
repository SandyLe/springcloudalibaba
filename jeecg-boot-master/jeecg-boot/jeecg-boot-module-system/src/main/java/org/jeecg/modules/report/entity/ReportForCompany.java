package org.jeecg.modules.report.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecg.modules.basic.entity.CBasicEntity;

@Data
@TableName("sl_report_for_company")
@ApiModel(value = "Report", description = "报表")
public class ReportForCompany extends CBasicEntity {

    @ApiModelProperty("报表")
    private String reportId;

}
