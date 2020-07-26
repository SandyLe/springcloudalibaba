package org.jeecg.modules.invoice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.invoice.entity.Invoice;
import org.jeecg.modules.invoice.mapper.InvoiceMapper;
import org.jeecg.modules.invoice.service.InvoiceService;
import org.springframework.stereotype.Service;

@Service
public class InvoiceServiceImpl extends ServiceImpl<InvoiceMapper, Invoice> implements InvoiceService {

    @Override
    public Invoice findBySouorceId(String sourceId) {

        LambdaQueryWrapper<Invoice> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Invoice::getSourceId, sourceId);
        return getOne(lambdaQueryWrapper);
    }
}
