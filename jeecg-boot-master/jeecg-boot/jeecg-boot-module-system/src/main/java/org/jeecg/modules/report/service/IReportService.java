package org.jeecg.modules.report.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.report.entity.Report;

/**
 * @Description:
 * @Author: tomkluas
 * @Date: 2020/6/23 11:16
 * @Version: V1.0
 */
public interface IReportService extends IService<Report> {
    int Save();
}
