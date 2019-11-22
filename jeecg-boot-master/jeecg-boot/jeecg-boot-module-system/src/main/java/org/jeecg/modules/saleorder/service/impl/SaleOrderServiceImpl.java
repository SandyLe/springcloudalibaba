package org.jeecg.modules.saleorder.service.impl;

import org.jeecg.modules.saleorder.entity.SaleOrder;
import org.jeecg.modules.saleorder.mapper.SaleOrderMapper;
import org.jeecg.modules.saleorder.service.SaleOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SaleOrderServiceImpl implements SaleOrderService {

    @Autowired
    private SaleOrderMapper saleOrderMapper;

    @Override
    public void save(SaleOrder entity) {
        saleOrderMapper.insert(entity);
    }
}
