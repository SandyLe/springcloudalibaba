package org.jeecg.modules.basic.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.basic.entity.LogisticsCompany;
import org.jeecg.modules.basic.mapper.LogisticsCompanyMapper;
import org.jeecg.modules.basic.service.LogisticsCompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LogisticsCompanyServiceImpl extends ServiceImpl<LogisticsCompanyMapper, LogisticsCompany> implements LogisticsCompanyService {

    @Autowired
    private LogisticsCompanyMapper logisticsCompanyMapper;
}
