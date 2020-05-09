package org.jeecg.modules.cost.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.jeecg.modules.cost.entity.PurchaseCost;
import org.jeecg.modules.cost.mapper.PurchaseCostMapper;
import org.jeecg.modules.cost.service.PurchaseCostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PurchaseCostServiceImpl extends ServiceImpl<PurchaseCostMapper, PurchaseCost> implements PurchaseCostService {

    @Autowired
    private PurchaseCostMapper purchaseCostMapper;

    @Override
    public PurchaseCost findMtlPurchaseCost(String mtlId, String unitId, String batchNo) {
        return findMtlPurchaseCost(null, mtlId, unitId, batchNo);
    }
    private PurchaseCost findMtlPurchaseCost(String ccId, String mtlId, String unitId, String batchNo) {

        LambdaQueryWrapper<PurchaseCost> lambdaQueryWrapper = new LambdaQueryWrapper<PurchaseCost>();
        if (StringUtils.isNotBlank(ccId)) {
            lambdaQueryWrapper.eq(PurchaseCost::getCompanyId, ccId);
        }
        if (StringUtils.isNotBlank(mtlId)) {
            lambdaQueryWrapper.eq(PurchaseCost::getMtlId, mtlId);
        }
        if (StringUtils.isNotBlank(unitId)) {
            lambdaQueryWrapper.eq(PurchaseCost::getUnitId, unitId);
        }
        if (StringUtils.isNotBlank(batchNo)) {
            lambdaQueryWrapper.eq(PurchaseCost::getBatchNo, batchNo);
        }
        return getOne(lambdaQueryWrapper);
    }

    @Override
    public boolean save(PurchaseCost entity) {
        return super.save(entity);
    }
}
