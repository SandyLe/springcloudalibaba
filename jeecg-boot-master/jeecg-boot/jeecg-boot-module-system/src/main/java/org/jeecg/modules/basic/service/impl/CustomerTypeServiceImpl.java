package org.jeecg.modules.basic.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.basic.entity.CustomerType;
import org.jeecg.modules.basic.mapper.CustomerTypeMapper;
import org.jeecg.modules.basic.service.CustomerTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerTypeServiceImpl extends ServiceImpl<CustomerTypeMapper, CustomerType> implements CustomerTypeService{

    @Autowired
    private CustomerTypeMapper customerTypeMapper;
}
