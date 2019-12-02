package org.jeecg.modules.basic.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.basic.entity.Customer;
import org.jeecg.modules.basic.mapper.CustomerMapper;
import org.jeecg.modules.basic.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerServiceImpl extends ServiceImpl<CustomerMapper, Customer> implements CustomerService{

    @Autowired
    private CustomerMapper customerMapper;
}
