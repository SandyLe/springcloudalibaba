package org.jeecg.modules.financial.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.common.enums.BillType;
import org.jeecg.common.enums.ReceiptOrderStatus;
import org.jeecg.common.enums.ReceiptStatus;
import org.jeecg.modules.financial.entity.ReceiptOrder;
import org.jeecg.modules.financial.entity.ReceiptOrderDtl;
import org.jeecg.modules.financial.mapper.ReceiptOrderDtlMapper;
import org.jeecg.modules.financial.service.ReceiptOrderDtlService;
import org.jeecg.modules.financial.service.ReceiptOrderService;
import org.jeecg.modules.saleorder.entity.SaleOrder;
import org.jeecg.modules.saleorder.service.SaleOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ReceiptOrderDtlServiceImpl extends ServiceImpl<ReceiptOrderDtlMapper, ReceiptOrderDtl> implements ReceiptOrderDtlService {

    @Autowired
    private SaleOrderService saleOrderService;

    @Autowired
    private ReceiptOrderService receiptOrderService;

    @Override
    public boolean saveOrUpdate(ReceiptOrderDtl entity) {

        Boolean flag = super.saveOrUpdate(entity);
        updateBillStatus(entity);
        return flag;
    }

    @Override
    public void updateBillStatus(ReceiptOrderDtl entity) {

        ReceiptOrder receiptOrder = receiptOrderService.getById(entity.getSourceId());
        List<ReceiptOrderDtl> dtlList = findBySourceId(receiptOrder.getId());
        BigDecimal paied = dtlList.stream().map(ReceiptOrderDtl::getPayAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
        if (paied.compareTo(receiptOrder.getAmount()) >= 0) {
            receiptOrder.setBillStatusId(ReceiptOrderStatus.Finished.getId());
        } else {
            receiptOrder.setBillStatusId(ReceiptOrderStatus.PartialCollection.getId());
        }
        receiptOrderService.saveOrUpdate(receiptOrder);

        if (entity.getSourceBillType() == BillType.SALEORDER.getId()) {
            SaleOrder aReturn = saleOrderService.getById(entity.getSourceBillId());
            aReturn.setPayamount(paied);
            if (receiptOrder.getBillStatusId() == ReceiptOrderStatus.Finished.getId()) {
                aReturn.setReceiptStatus(ReceiptStatus.Finished.getId());
            } else if (receiptOrder.getBillStatusId() == ReceiptOrderStatus.PartialCollection.getId()) {
                aReturn.setReceiptStatus(ReceiptStatus.PartialPayment.getId());
            }
            saleOrderService.saveOrUpdate(aReturn);
        }
    }

    private List<ReceiptOrderDtl> findBySourceId(String sourceId) {

        LambdaQueryWrapper<ReceiptOrderDtl> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(ReceiptOrderDtl::getSourceId, sourceId);
        return super.list(lambdaQueryWrapper);
    }

}
