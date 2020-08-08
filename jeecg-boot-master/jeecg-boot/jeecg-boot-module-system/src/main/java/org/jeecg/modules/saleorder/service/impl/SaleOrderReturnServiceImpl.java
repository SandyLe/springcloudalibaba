package org.jeecg.modules.saleorder.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.saleorder.entity.SaleOrderReturn;
import org.jeecg.modules.saleorder.mapper.SaleOrderReturnMapper;
import org.jeecg.modules.saleorder.service.SaleOrderReturnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SaleOrderReturnServiceImpl extends ServiceImpl<SaleOrderReturnMapper, SaleOrderReturn>  implements SaleOrderReturnService {

    @Autowired
    private SaleOrderReturnMapper saleOrderReturnMapper;

    @Override
    public List<SaleOrderReturn> findBySourceId(String sourceId) {

        LambdaQueryWrapper<SaleOrderReturn> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(SaleOrderReturn::getSourceId, sourceId);
        return super.list(lambdaQueryWrapper);
    }
}
