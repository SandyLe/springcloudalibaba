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

        BigDecimal paied = BigDecimal.ZERO;
        ReceiptOrder receiptOrder = receiptOrderService.getById(entity.getSourceId());
        List<ReceiptOrderDtl> dtlList = super.list(new LambdaQueryWrapper<ReceiptOrderDtl>().eq(ReceiptOrderDtl::getSourceId, entity.getSourceId()));
        for (ReceiptOrderDtl receiptOrderDtl : dtlList) {
            paied = paied.add(receiptOrderDtl.getPayAmount());
        }

        if (paied.compareTo(receiptOrder.getAmount()) >= 0) {
            receiptOrder.setBillStatusId(ReceiptOrderStatus.Finished.getId());
        } else {
            receiptOrder.setBillStatusId(ReceiptOrderStatus.PartialCollection.getId());
        }
        receiptOrderService.saveOrUpdate(receiptOrder);

        if (entity.getSourceBillType() == BillType.SALEORDER.getId()) {

            SaleOrder saleOrder = saleOrderService.getById(entity.getSourceBillId());
            if (receiptOrder.getBillStatusId() == ReceiptOrderStatus.Finished.getId()) {
                saleOrder.setReceiptStatus(ReceiptStatus.Finished.getId());
            } else if (receiptOrder.getBillStatusId() == ReceiptOrderStatus.PartialCollection.getId()) {
                saleOrder.setReceiptStatus(ReceiptStatus.PartialPayment.getId());
            }
            saleOrderService.saveOrUpdate(saleOrder);
        }

        return flag;
    }


}
