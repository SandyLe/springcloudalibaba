package org.jeecg.modules.basic.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.basic.entity.CustomerDeliveryInfo;
import org.jeecg.modules.basic.mapper.CustomerDeliveryInfoMapper;
import org.jeecg.modules.basic.service.CustomerDeliveryInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class CustomerDeliveryInfoServiceImpl extends ServiceImpl<CustomerDeliveryInfoMapper, CustomerDeliveryInfo> implements CustomerDeliveryInfoService{

    @Autowired
    private CustomerDeliveryInfoMapper customerDeliveryInfoMapper;

}
