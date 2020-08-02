package org.jeecg.modules.financial.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.jeecg.common.enums.BillType;
import org.jeecg.modules.basic.service.BillCodeBuilderService;
import org.jeecg.modules.financial.entity.RefundOrder;
import org.jeecg.modules.financial.mapper.RefundOrderMapper;
import org.jeecg.modules.financial.service.RefundOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class RefundOrderServiceImpl extends ServiceImpl<RefundOrderMapper, RefundOrder> implements RefundOrderService {

    @Autowired
    private BillCodeBuilderService billCodeBuilderService;

    @Override
    public boolean saveOrUpdate(RefundOrder entity) {

        if (StringUtils.isBlank(entity.getId())) {
            entity.setCode(billCodeBuilderService.getBillCode(BillType.REFUND.getId()));
        }
        return super.saveOrUpdate(entity);
    }

    @Override
    public RefundOrder findBySourceBillId(String sourceBillId, Integer sourceBillType, String companyId) {

        LambdaQueryWrapper<RefundOrder> lambdaQueryWrapper = new LambdaQueryWrapper<RefundOrder>();
        lambdaQueryWrapper.eq(RefundOrder::getSourceId, sourceBillId);
        lambdaQueryWrapper.eq(RefundOrder::getSourceBillType, sourceBillType);
        lambdaQueryWrapper.eq(RefundOrder::getCompanyId, companyId);
        return super.getOne(lambdaQueryWrapper);
    }
}
