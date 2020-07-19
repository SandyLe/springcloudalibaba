package org.jeecg.modules.report.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.report.entity.Report;
import org.jeecg.modules.report.mapper.IReportMapper;
import org.jeecg.modules.report.service.IReportService;
import org.springframework.stereotype.Service;

/**
 * @Description:
 * @Author: tomkluas
 * @Date: 2020/6/23 11:17
 * @Version: V1.0
 */
@Service
public class ReportServiceImpl extends ServiceImpl<IReportMapper, Report> implements IReportService {

    @Override
    public int Save(){
        return 0;
    }
}
