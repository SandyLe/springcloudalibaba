package org.jeecg.modules.saleorder.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.saleorder.entity.SaleOrderDeliveryInfo;
import org.jeecg.modules.saleorder.mapper.SaleOrderDeliveryInfoMapper;
import org.jeecg.modules.saleorder.service.SaleOrderDeliveryInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SaleOrderDeliveryInfoServiceImpl extends ServiceImpl<SaleOrderDeliveryInfoMapper, SaleOrderDeliveryInfo> implements SaleOrderDeliveryInfoService{

    @Autowired
    private SaleOrderDeliveryInfoMapper saleOrderDeliveryInfoMapper;

}
