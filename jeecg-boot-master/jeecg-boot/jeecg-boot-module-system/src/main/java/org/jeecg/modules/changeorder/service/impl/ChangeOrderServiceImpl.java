package org.jeecg.modules.changeorder.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.jeecg.common.enums.*;
import org.jeecg.common.util.LoginUtils;
import org.jeecg.modules.basic.service.BillCodeBuilderService;
import org.jeecg.modules.financial.entity.ReceiptOrder;
import org.jeecg.modules.financial.entity.RefundOrder;
import org.jeecg.modules.financial.service.ReceiptOrderService;
import org.jeecg.modules.financial.service.RefundOrderService;
import org.jeecg.modules.inventory.entity.InventoryIn;
import org.jeecg.modules.inventory.entity.InventoryOut;
import org.jeecg.modules.inventory.service.InventoryInService;
import org.jeecg.modules.inventory.service.InventoryOutService;
import org.jeecg.modules.changeorder.dto.ChangeOrderDto;
import org.jeecg.modules.changeorder.entity.ChangeOrder;
import org.jeecg.modules.changeorder.entity.ChangeOrderDtl;
import org.jeecg.modules.changeorder.mapper.ChangeOrderMapper;
import org.jeecg.modules.changeorder.service.ChangeOrderDtlService;
import org.jeecg.modules.changeorder.service.ChangeOrderService;
import org.jeecg.modules.saleorder.entity.SaleOrder;
import org.jeecg.modules.saleorder.service.SaleOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description:
 * @Author: lixt
 * @Date: 2020/04/04 16:13
 * @Version: V1.0
 */
@Service
public class ChangeOrderServiceImpl extends ServiceImpl<ChangeOrderMapper, ChangeOrder> implements ChangeOrderService {

    @Autowired
    private ChangeOrderDtlService changeOrderDtlService;
    @Lazy
    @Autowired
    private InventoryOutService inventoryOutService;
    @Lazy
    @Autowired
    private InventoryInService inventoryInService;
    @Autowired
    private BillCodeBuilderService billCodeBuilderService;
    @Autowired
    private RefundOrderService refundOrderService;
    @Autowired
    private ReceiptOrderService receiptOrderService;
    @Autowired
    private SaleOrderService saleOrderService;

    @Override
    @Transactional
    public String saveOrder(ChangeOrderDto changeOrderdto){
        // 换货主表
        String code = billCodeBuilderService.getBillCode(BillType.CHANGEORDER.getId());
        changeOrderdto.setCode(code);
        changeOrderdto.setBillStatus(BillStatus.NEW.getId());
        super.save(changeOrderdto);

        //换货单子表
        if (CollectionUtils.isNotEmpty(changeOrderdto.getDetaillist())){
            BigDecimal totalReceipt = BigDecimal.ZERO;
            BigDecimal totalRefund = BigDecimal.ZERO;
            List<ChangeOrderDtl> mtls = changeOrderdto.getDetaillist().stream().filter(o-> StringUtils.isNotBlank(o.getMtlId())).collect(Collectors.toList());
            for (ChangeOrderDtl dtl : mtls) {
                //组装商品详情
                dtl.setCode(code);
                dtl.setCompanyId(changeOrderdto.getCompanyId());
                dtl.setSourceId(changeOrderdto.getId());
                if (dtl.getPriceSpaceModeId() == PriceSpaceMode.Receive.getId() && null != dtl.getPriceSpace()) {
                    totalReceipt = totalReceipt.add(dtl.getPriceSpace());
                }
                if (dtl.getPriceSpaceModeId() == PriceSpaceMode.Refund.getId() && null != dtl.getPriceSpace()) {
                    totalRefund = totalRefund.add(dtl.getPriceSpace());
                }
            }
            changeOrderDtlService.saveBatch(mtls);

            //收款单
            if (totalReceipt.compareTo(BigDecimal.ZERO) > 0) {

                ReceiptOrder receiptOrder = new ReceiptOrder();
                receiptOrder.setSourceId(changeOrderdto.getId());
                receiptOrder.setSourceBillCode(changeOrderdto.getCode());
                receiptOrder.setBillStatusId(ReceiptOrderStatus.AgencyCollection.getId());
                receiptOrder.setSalemanId(LoginUtils.getLoginUser().getId());
                receiptOrder.setBillDate(new Date());
                receiptOrder.setSourceBillType(changeOrderdto.getBillType());
                receiptOrder.setAmount(totalReceipt);
                receiptOrder.setCompanyId(changeOrderdto.getCompanyId());

                SaleOrder saleOrder = saleOrderService.getOneByCode(changeOrderdto.getSourceCode(), changeOrderdto.getCompanyId());
                receiptOrder.setPayerId(saleOrder.getCustomerId());
                receiptOrderService.saveOrUpdate(receiptOrder);
            }
            //退款单
            if (totalRefund.compareTo(BigDecimal.ZERO) > 0) {

                RefundOrder refundOrder = new RefundOrder();
                refundOrder.setSourceId(changeOrderdto.getId());
                refundOrder.setSourceBillCode(changeOrderdto.getCode());
                refundOrder.setBillStatusId(ReceiptOrderStatus.AgencyCollection.getId());
                refundOrder.setSalemanId(LoginUtils.getLoginUser().getId());
                refundOrder.setBillDate(new Date());
                refundOrder.setSourceBillType(changeOrderdto.getBillType());
                refundOrder.setAmount(totalRefund);
                refundOrder.setCompanyId(changeOrderdto.getCompanyId());

                SaleOrder saleOrder = saleOrderService.getOneByCode(changeOrderdto.getSourceCode(), changeOrderdto.getCompanyId());
                refundOrder.setPayerId(saleOrder.getCustomerId());
                refundOrderService.saveOrUpdate(refundOrder);
            }
        }

        // 换货出库通知
        InventoryOut inventoryOut = new InventoryOut(changeOrderdto.getId(), changeOrderdto.getCode(), BillType.INVENTORYOUT.getId(), BillType.CHANGEORDER.getId(), BillStatus.TOSTOCKOUT.getId());
        inventoryOut.setRowSts(RowSts.EFFECTIVE.getId());
        inventoryOut.setCompanyId(changeOrderdto.getCompanyId());
        inventoryOutService.saveToInventoryOut(inventoryOut);
        // 换货入库通知
        InventoryIn inventoryIn = new InventoryIn();
        inventoryIn.setCompanyId(changeOrderdto.getCompanyId());
        inventoryIn.setBillStatus(BillStatus.TOSTOCKIN.getId());
        inventoryIn.setSourceCode(code);
        inventoryIn.setSourceId(changeOrderdto.getId());
        inventoryIn.setBillType(BillType.INVENTORYIN.getId());
        inventoryIn.setRowSts(RowSts.EFFECTIVE.getId());
        inventoryIn.setSourceBillType(BillType.CHANGEORDER.getId());
        inventoryIn.setCode(billCodeBuilderService.getBillCode(BillType.INVENTORYIN.getId()));
        inventoryInService.saveToInventoryIn(inventoryIn);

        return changeOrderdto.getId();
    }

    @Override
    @Transactional
    public String editOrder(ChangeOrderDto changeOrderdto){

        // 组装主表
        super.updateById(changeOrderdto);
        if (changeOrderdto.getDetaillist().size() > 0){
            BigDecimal totalReceipt = BigDecimal.ZERO;
            BigDecimal totalRefund = BigDecimal.ZERO;
            for (ChangeOrderDtl item: changeOrderdto.getDetaillist()){

                if (StringUtils.isBlank(item.getCompanyId())) {
                    item.setCompanyId(changeOrderdto.getCompanyId());
                }
                //组装商品详情
                if (item.getId() != null && item.getId().length() > 0)
                    changeOrderDtlService.updateById(item);
                else{
                    item.setSourceId(changeOrderdto.getId());
                    changeOrderDtlService.save(item);
                }
                if (item.getPriceSpaceModeId() == PriceSpaceMode.Receive.getId() && null != item.getPriceSpace()) {
                    totalReceipt = totalReceipt.add(item.getPriceSpace());
                }
                if (item.getPriceSpaceModeId() == PriceSpaceMode.Refund.getId() && null != item.getPriceSpace()) {
                    totalRefund = totalRefund.add(item.getPriceSpace());
                }
            }
            //收款单
            if (totalReceipt.compareTo(BigDecimal.ZERO) > 0) {
                ReceiptOrder receiptOrder = receiptOrderService.findBySourceBillId(changeOrderdto.getId(), changeOrderdto.getBillType(), changeOrderdto.getCompanyId());
                if ( null == receiptOrder ){
                    receiptOrder = new ReceiptOrder();
                    receiptOrder.setSourceId(changeOrderdto.getId());
                    receiptOrder.setSourceBillCode(changeOrderdto.getCode());
                    receiptOrder.setBillStatusId(ReceiptOrderStatus.AgencyCollection.getId());
                    receiptOrder.setSalemanId(LoginUtils.getLoginUser().getId());
                    receiptOrder.setBillDate(new Date());
                    receiptOrder.setSourceBillType(changeOrderdto.getBillType());
                    receiptOrder.setAmount(totalReceipt);
                    receiptOrder.setCompanyId(changeOrderdto.getCompanyId());

                    SaleOrder saleOrder = saleOrderService.getOneByCode(changeOrderdto.getSourceCode(), changeOrderdto.getCompanyId());
                    receiptOrder.setPayerId(saleOrder.getCustomerId());

                }
                receiptOrder.setAmount(totalReceipt);
                receiptOrderService.saveOrUpdate(receiptOrder);
            }
            //退款单
            if (totalRefund.compareTo(BigDecimal.ZERO) > 0) {
                RefundOrder refundOrder = refundOrderService.findBySourceBillId(changeOrderdto.getId(), changeOrderdto.getBillType(), changeOrderdto.getCompanyId());
                if ( null == refundOrder ){
                    refundOrder = new RefundOrder();
                    refundOrder.setSourceId(changeOrderdto.getId());
                    refundOrder.setSourceBillCode(changeOrderdto.getCode());
                    refundOrder.setBillStatusId(ReceiptOrderStatus.AgencyCollection.getId());
                    refundOrder.setSalemanId(LoginUtils.getLoginUser().getId());
                    refundOrder.setBillDate(new Date());
                    refundOrder.setSourceBillType(changeOrderdto.getBillType());
                    refundOrder.setAmount(totalRefund);
                    refundOrder.setCompanyId(changeOrderdto.getCompanyId());

                    SaleOrder saleOrder = saleOrderService.getOneByCode(changeOrderdto.getSourceCode(), changeOrderdto.getCompanyId());
                    refundOrder.setPayerId(saleOrder.getCustomerId());

                }
                refundOrder.setAmount(totalRefund);
                refundOrderService.saveOrUpdate(refundOrder);
            }
        }


        // 配件出库
        inventoryOutService.deleteBySourceId(changeOrderdto.getBillType(), changeOrderdto.getId());
        InventoryOut inventoryOut = new InventoryOut(changeOrderdto.getId(), changeOrderdto.getCode(), BillType.INVENTORYOUT.getId(), BillType.CHANGEORDER.getId(), BillStatus.TOSTOCKOUT.getId());
        inventoryOut.setRowSts(RowSts.EFFECTIVE.getId());
        inventoryOut.setCompanyId(changeOrderdto.getCompanyId());
        inventoryOutService.saveToInventoryOut(inventoryOut);


        inventoryInService.deleteBySourceId(changeOrderdto.getBillType(), changeOrderdto.getId());
        InventoryIn inventoryIn = new InventoryIn();
        inventoryIn.setCompanyId(changeOrderdto.getCompanyId());
        inventoryIn.setBillStatus(BillStatus.TOSTOCKIN.getId());
        inventoryIn.setSourceCode(changeOrderdto.getCode());
        inventoryIn.setSourceId(changeOrderdto.getId());
        inventoryIn.setBillType(BillType.INVENTORYIN.getId());
        inventoryIn.setRowSts(RowSts.EFFECTIVE.getId());
        inventoryIn.setSourceBillType(BillType.CHANGEORDER.getId());
        inventoryIn.setCode(billCodeBuilderService.getBillCode(BillType.INVENTORYIN.getId()));
        inventoryInService.saveToInventoryIn(inventoryIn);
        return changeOrderdto.getId();
    }
}
