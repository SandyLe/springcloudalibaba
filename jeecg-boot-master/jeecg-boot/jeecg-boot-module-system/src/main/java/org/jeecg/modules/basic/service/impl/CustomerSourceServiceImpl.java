package org.jeecg.modules.basic.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.basic.entity.CustomerSource;
import org.jeecg.modules.basic.mapper.CustomerSourceMapper;
import org.jeecg.modules.basic.mapper.CustomerTypeMapper;
import org.jeecg.modules.basic.service.CustomerSourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerSourceServiceImpl extends ServiceImpl<CustomerSourceMapper, CustomerSource> implements CustomerSourceService {

    @Autowired
    private CustomerSourceMapper customerSourceMapper;
}
