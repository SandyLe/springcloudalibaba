package org.jeecg.modules.financial.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.common.enums.BillType;
import org.jeecg.common.enums.ReceiptStatus;
import org.jeecg.common.enums.RefundOrderStatus;
import org.jeecg.modules.financial.entity.RefundOrder;
import org.jeecg.modules.financial.entity.RefundOrderDtl;
import org.jeecg.modules.financial.mapper.RefundOrderDtlMapper;
import org.jeecg.modules.financial.service.RefundOrderDtlService;
import org.jeecg.modules.financial.service.RefundOrderService;
import org.jeecg.modules.saleorder.entity.SaleOrderReturn;
import org.jeecg.modules.saleorder.service.SaleOrderReturnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class RefundOrderDtlServiceImpl extends ServiceImpl<RefundOrderDtlMapper, RefundOrderDtl> implements RefundOrderDtlService {

    @Autowired
    private SaleOrderReturnService saleOrderReturnService;

    @Autowired
    private RefundOrderService refundOrderService;

    @Override
    public boolean saveOrUpdate(RefundOrderDtl entity) {

        Boolean flag = super.saveOrUpdate(entity);

        updateBillStatus(entity);

        return flag;
    }

    @Override
    public void updateBillStatus(RefundOrderDtl entity) {

        RefundOrder refundOrder = refundOrderService.getById(entity.getSourceId());
        List<RefundOrderDtl> dtlList = findBySourceId(refundOrder.getId());
        BigDecimal paied = dtlList.stream().map(RefundOrderDtl::getPayAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
        if (paied.compareTo(refundOrder.getAmount()) >= 0) {
            refundOrder.setBillStatusId(RefundOrderStatus.Finished.getId());
        } else {
            refundOrder.setBillStatusId(RefundOrderStatus.PartialRefund.getId());
        }
        refundOrderService.saveOrUpdate(refundOrder);

        if (entity.getSourceBillType() == BillType.SALERETURNORDER.getId()) {
            SaleOrderReturn aReturn = saleOrderReturnService.getById(entity.getSourceBillId());
            aReturn.setPayamount(paied);
            if (refundOrder.getBillStatusId() == RefundOrderStatus.Finished.getId()) {
                aReturn.setRefundStatusId(ReceiptStatus.Finished.getId());
            } else if (refundOrder.getBillStatusId() == RefundOrderStatus.PartialRefund.getId()) {
                aReturn.setRefundStatusId(ReceiptStatus.PartialPayment.getId());
            }
            saleOrderReturnService.saveOrUpdate(aReturn);
        }
    }

    private List<RefundOrderDtl> findBySourceId(String sourceId) {

        LambdaQueryWrapper<RefundOrderDtl> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(RefundOrderDtl::getSourceId, sourceId);
        return super.list(lambdaQueryWrapper);
    }
}
