package org.jeecg.modules.invoice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.invoice.entity.InvoiceAddress;
import org.jeecg.modules.invoice.mapper.InvoiceAddressMapper;
import org.jeecg.modules.invoice.service.InvoiceAddressService;
import org.springframework.stereotype.Service;

@Service
public class InvoiceAddressServiceImpl extends ServiceImpl<InvoiceAddressMapper, InvoiceAddress> implements InvoiceAddressService {

    @Override
    public InvoiceAddress findBySouorceId(String sourceId) {

        LambdaQueryWrapper<InvoiceAddress> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(InvoiceAddress::getSourceId, sourceId);
        return getOne(lambdaQueryWrapper);
    }
}
