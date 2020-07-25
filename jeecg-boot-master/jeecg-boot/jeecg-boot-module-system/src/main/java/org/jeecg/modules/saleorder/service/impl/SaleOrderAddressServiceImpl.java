package org.jeecg.modules.saleorder.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.saleorder.entity.SaleOrderAddress;
import org.jeecg.modules.saleorder.mapper.SaleOrderAddressMapper;
import org.jeecg.modules.saleorder.service.SaleOrderAddressService;
import org.springframework.stereotype.Service;

@Service
public class SaleOrderAddressServiceImpl extends ServiceImpl<SaleOrderAddressMapper, SaleOrderAddress> implements SaleOrderAddressService {

    @Override
    public SaleOrderAddress findBySouorceId(String sourceId) {

        LambdaQueryWrapper<SaleOrderAddress> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(SaleOrderAddress::getSourceId, sourceId);
        return getOne(lambdaQueryWrapper);
    }
}
