package org.jeecg.modules.report.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.report.entity.Report;
import org.jeecg.modules.report.entity.ReportForCompany;

/**
 * @Description:
 * @Author: tomkluas
 * @Date: 2020/6/23 11:16
 * @Version: V1.0
 */
public interface IReportForCompanyService extends IService<ReportForCompany> {
    int Save();
}
