package org.jeecg.modules.financial.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.jeecg.common.enums.BillType;
import org.jeecg.common.enums.ReceiptOrderStatus;
import org.jeecg.modules.basic.service.BillCodeBuilderService;
import org.jeecg.modules.financial.entity.ReceiptOrder;
import org.jeecg.modules.financial.mapper.ReceiptOrderMapper;
import org.jeecg.modules.financial.service.ReceiptOrderService;
import org.jeecg.modules.saleorder.entity.SaleOrder;
import org.jeecg.modules.saleorder.service.SaleOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;

@Service
public class ReceiptOrderServiceImpl extends ServiceImpl<ReceiptOrderMapper, ReceiptOrder> implements ReceiptOrderService {

    @Autowired
    private SaleOrderService saleOrderService;

    @Autowired
    private BillCodeBuilderService billCodeBuilderService;

    @Override
    public boolean saveOrUpdate (ReceiptOrder receipt) {
        if (StringUtils.isBlank(receipt.getId())) {
            receipt.setCode(billCodeBuilderService.getBillCode(BillType.RECEIPTORDER.getId()));
        }
        return super.saveOrUpdate(receipt);
    }

    @Override
    public String createReceipt(String sourceId, Integer sourceBillType, String ccId) {

        BigDecimal totalAmount = BigDecimal.ZERO;
        Date billDate = new Date();
        String payerId = null;
        String salemanId = null;
        String sourceCode = null;

        if (sourceBillType == BillType.SALEORDER.getId()) {

            SaleOrder saleOrder = saleOrderService.getById(sourceId);
            totalAmount = saleOrder.getTotalamount();
            billDate = saleOrder.getBillDate();
            payerId = saleOrder.getCustomerId();
            salemanId = saleOrder.getSalemanId();
            sourceCode = saleOrder.getCode();
        }

        LambdaQueryWrapper<ReceiptOrder> lambdaQueryWrapper = new LambdaQueryWrapper<ReceiptOrder>();
        lambdaQueryWrapper.eq(ReceiptOrder::getSourceId, sourceId);
        lambdaQueryWrapper.eq(ReceiptOrder::getSourceBillType, sourceBillType);
        lambdaQueryWrapper.eq(ReceiptOrder::getCompanyId, ccId);
        ReceiptOrder receipt = super.getOne(lambdaQueryWrapper);
        if (null == receipt) {
            receipt = new ReceiptOrder();
            receipt.setBillStatusId(ReceiptOrderStatus.AgencyCollection.getId());
        }
        receipt.setCompanyId(ccId);
        receipt.setAmount(totalAmount);
        receipt.setBillDate(billDate);
        receipt.setPayerId(payerId);
        receipt.setSalemanId(salemanId);
        receipt.setSourceBillType(sourceBillType);
        receipt.setSourceId(sourceId);
        receipt.setSourceBillCode(sourceCode);

        saveOrUpdate(receipt);
        return receipt.getId();
    }

    @Override
    public ReceiptOrder findBySourceBillId(String sourceBillId, Integer sourceBillType, String companyId) {

        LambdaQueryWrapper<ReceiptOrder> lambdaQueryWrapper = new LambdaQueryWrapper<ReceiptOrder>();
        lambdaQueryWrapper.eq(ReceiptOrder::getSourceId, sourceBillId);
        lambdaQueryWrapper.eq(ReceiptOrder::getSourceBillType, sourceBillType);
        lambdaQueryWrapper.eq(ReceiptOrder::getCompanyId, companyId);
        return super.getOne(lambdaQueryWrapper);
    }
}
