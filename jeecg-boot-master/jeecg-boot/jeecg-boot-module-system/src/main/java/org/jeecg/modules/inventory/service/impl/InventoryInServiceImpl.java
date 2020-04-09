package org.jeecg.modules.inventory.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.jeecg.modules.basic.enums.BillStatus;
import org.jeecg.modules.basic.enums.BillType;
import org.jeecg.modules.basic.enums.InventoryOperation;
import org.jeecg.modules.basic.enums.RowSts;
import org.jeecg.modules.cost.entity.PurchaseCost;
import org.jeecg.modules.cost.service.PurchaseCostService;
import org.jeecg.modules.inventory.dto.PreInventoryOutMtl;
import org.jeecg.modules.inventory.entity.AllotDtl;
import org.jeecg.modules.inventory.entity.InventoryIn;
import org.jeecg.modules.inventory.entity.InventoryInMtl;
import org.jeecg.modules.inventory.entity.InventoryLog;
import org.jeecg.modules.inventory.mapper.InventoryInMapper;
import org.jeecg.modules.inventory.service.*;
import org.jeecg.modules.purchase.entity.Purchase;
import org.jeecg.modules.purchase.entity.PurchaseMtl;
import org.jeecg.modules.purchase.service.PurchaseMtlService;
import org.jeecg.modules.purchase.service.PurchaseService;
import org.jeecg.modules.saleorder.entity.SaleOrderReturn;
import org.jeecg.modules.saleorder.entity.SaleOrderReturnMtl;
import org.jeecg.modules.saleorder.service.SaleOrderReturnMtlService;
import org.jeecg.modules.saleorder.service.SaleOrderReturnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class InventoryInServiceImpl extends ServiceImpl<InventoryInMapper, InventoryIn> implements InventoryInService {
/*
    @Autowired
    private InventoryInMapper inventoryInMapper;
*/

    @Autowired
    private PurchaseMtlService purchaseMtlService;
    @Autowired
    private SaleOrderReturnMtlService saleOrderReturnMtlService;
    @Autowired
    private InventoryInMtlService inventoryInMtlService;
    @Autowired
    private InventoryLogService inventoryLogService;
    @Autowired
    private InventoryService inventoryService;
    @Autowired
    private PurchaseService purchaseService;
    @Autowired
    private SaleOrderReturnService saleOrderReturnService;
    @Autowired
    private PurchaseCostService purchaseCostService;
    @Autowired
    private AllotDtlService allotDtlService;


    @Override
    public List<PreInventoryOutMtl> getDeliveryMtlList(String id, String sourceId) {
        List<PreInventoryOutMtl> preInventoryOutMtls = Lists.newArrayList();
        LambdaQueryWrapper<InventoryInMtl> querySaleMtlWrapper = new QueryWrapper<InventoryInMtl>().lambda().eq(InventoryInMtl::getSourceBillId, sourceId).eq(InventoryInMtl::getRowSts, RowSts.EFFECTIVE.getId());
        LambdaQueryWrapper<InventoryLog> queryInventoryLogWrapper = new QueryWrapper<InventoryLog>().lambda().eq(InventoryLog::getSourceId, sourceId);
        List<InventoryInMtl> inventoryInMtls = inventoryInMtlService.list(querySaleMtlWrapper);
        List<InventoryLog> inventoryLogs = inventoryLogService.list(queryInventoryLogWrapper);
        Map<String, BigDecimal> mtlDeliveryQtyMap = new HashMap<>();
        inventoryLogs.stream().forEach(o->{
            mtlDeliveryQtyMap.put(o.getMtlId(), null == mtlDeliveryQtyMap.get(o.getMtlId()) ? o.getOptAmount() : mtlDeliveryQtyMap.get(o.getMtlId()).add(o.getOptAmount()));
        });
        inventoryInMtls.forEach(o->{
            BigDecimal tempAmout = o.getQuantity();
            if (null != mtlDeliveryQtyMap.get(o.getMtlId())) {
                tempAmout = tempAmout.subtract(mtlDeliveryQtyMap.get(o.getMtlId()));
            }
            if (tempAmout.compareTo(BigDecimal.ZERO) > 0 ) {
                PreInventoryOutMtl preInventoryOutMtl = new PreInventoryOutMtl();
                preInventoryOutMtl.setBillId(id);
                preInventoryOutMtl.setSourceId(sourceId);
                preInventoryOutMtl.setSourceBillType(o.getSourceBillType());
                preInventoryOutMtl.setMtlId(o.getMtlId());
                preInventoryOutMtl.setQuantity(tempAmout);
                preInventoryOutMtl.setUnitId(o.getUnitId());
                preInventoryOutMtls.add(preInventoryOutMtl);
            }
        });
        return preInventoryOutMtls;
    }

    @Override
    public String saveToInventoryIn(InventoryIn inventoryIn) {

        // 保存主表
        save(inventoryIn);

        // 保存子表
        List<InventoryInMtl> inventoryInMtls = Lists.newArrayList();
        if (inventoryIn.getSourceBillType() == BillType.PURCHASEORDER.getId()) {
            LambdaQueryWrapper<PurchaseMtl> queryWrapper = new LambdaQueryWrapper<PurchaseMtl>().eq(PurchaseMtl::getSourceId, inventoryIn.getSourceId());
            List<PurchaseMtl> purchaseDtls = purchaseMtlService.list(queryWrapper);
            if (CollectionUtils.isNotEmpty(purchaseDtls)) {
                purchaseDtls.forEach(o ->{
                    inventoryInMtls.add(new InventoryInMtl(inventoryIn.getId(), o.getSourceId(), inventoryIn.getSourceBillType(), o.getMtlId(), o.getQuantity(), o.getUnitId(), RowSts.EFFECTIVE.getId()));
                });
            }
        } else if (inventoryIn.getSourceBillType() == BillType.SALERETURNORDER.getId()) {
            LambdaQueryWrapper<SaleOrderReturnMtl> queryWrapper = new LambdaQueryWrapper<SaleOrderReturnMtl>().eq(SaleOrderReturnMtl::getSourceId, inventoryIn.getSourceId());
            List<SaleOrderReturnMtl> saleOrderReturnMtls = saleOrderReturnMtlService.list(queryWrapper);
            if (CollectionUtils.isNotEmpty(saleOrderReturnMtls)) {
                saleOrderReturnMtls.forEach(o ->{
                    inventoryInMtls.add(new InventoryInMtl(inventoryIn.getId(), o.getSourceId(), inventoryIn.getSourceBillType(), o.getMtlId(), o.getQuantity(), o.getUnitId(), RowSts.EFFECTIVE.getId()));
                });
            }
        } else if (inventoryIn.getSourceBillType() == BillType.ALLOT.getId()) {
            LambdaQueryWrapper<AllotDtl> queryWrapper = new LambdaQueryWrapper<AllotDtl>().eq(AllotDtl::getSourceId, inventoryIn.getSourceId());
            List<AllotDtl> allotDtls = allotDtlService.list(queryWrapper);
            if (CollectionUtils.isNotEmpty(allotDtls)) {
                allotDtls.forEach(o ->{
                    inventoryInMtls.add(new InventoryInMtl(inventoryIn.getId(), o.getSourceId(), inventoryIn.getSourceBillType(), o.getMtlId(), o.getAllotAmount(), o.getUnitId(), RowSts.EFFECTIVE.getId()));
                });
            }
        }
        inventoryInMtlService.saveBatch(inventoryInMtls);
        return inventoryIn.getId();
    }

    @Transactional
    @Override
    public Boolean stockIn(List<PreInventoryOutMtl> mtls) {

        InventoryIn info = getById(mtls.get(0).getBillId());
        if (null != info) {
            Integer billType = info.getSourceBillType();
            SaleOrderReturn saleOrderReturn = null;
            Purchase purchase = null;
            String batchNo = null;
            Integer billStatus = BillStatus.ALLSTOCKINED.getId();
            Integer operationId = null;
            if (billType == BillType.PURCHASEORDER.getId()) {
                purchase = purchaseService.getById(info.getSourceId());
                batchNo = purchase.getBatchNo();
                operationId = InventoryOperation.PURCHASEIN.getId();
            } else if (billType == BillType.SALERETURNORDER.getId()) {
                saleOrderReturn = saleOrderReturnService.getById(info.getSourceId());
                operationId = InventoryOperation.SALERETURNIN.getId();
            } else if (billType == BillType.ALLOT.getId()) {
                operationId = InventoryOperation.ALLOTIN.getId();
            } else if (billType == BillType.ASSEMBLE.getId()) {
                operationId = InventoryOperation.ASSEMBLEIN.getId();
            } else if (billType == BillType.TEARDOWN.getId()) {
                operationId = InventoryOperation.TEARDOWNIN.getId();
            }
            for (PreInventoryOutMtl mtl : mtls) {
                // 入库更新库存
                InventoryLog inventoryLog = new InventoryLog(mtl.getSourceId(), info.getSourceBillType(), mtl.getMtlId(),
                        info.getWarehouseId(), null, mtl.getQuantity(), null, mtl.getUnitId(), operationId, batchNo);
                inventoryService.updateInventory(inventoryLog);

                // 更新采购产品批次平均价格
                if (billType == BillType.PURCHASEORDER.getId()) {
                    if (StringUtils.isNotBlank(batchNo)) {

                        List<PurchaseMtl> purchaseMtls = purchaseMtlService.findPurchaseMtls(batchNo, mtl.getMtlId());
                        purchaseMtls = purchaseMtls.stream().filter(o->o.getUnitId().equals(mtl.getUnitId())).collect(Collectors.toList());
                        if (CollectionUtils.isNotEmpty(purchaseMtls)) {
                            BigDecimal totalAmount = BigDecimal.ZERO;
                            BigDecimal totalQty = BigDecimal.ZERO;
                            for (PurchaseMtl purchaseMtl: purchaseMtls){
                                totalAmount = totalAmount.add(purchaseMtl.getAmount());
                                totalQty = totalQty.add(purchaseMtl.getQuantity());
                            }
                            PurchaseCost purchaseCost = purchaseCostService.findMtlPurchaseCost(mtl.getMtlId(), mtl.getUnitId(), batchNo);
                            if (null == purchaseCost) {
                                purchaseCost = new PurchaseCost();
                                purchaseCost.setBatchNo(batchNo);
                                purchaseCost.setMtlId(mtl.getMtlId());
                                purchaseCost.setUnitId(mtl.getUnitId());
                            }
                            purchaseCost.setAveragePrice(BigDecimal.ZERO.compareTo(totalQty) < 0 ? totalAmount.divide(totalQty, 6, BigDecimal.ROUND_HALF_UP) : BigDecimal.ZERO);
                            purchaseCostService.saveOrUpdate(purchaseCost);
                        }
                    }
                }

            }
            List<PreInventoryOutMtl> deliveryMtls = getDeliveryMtlList(info.getId(), info.getSourceId());
            if (CollectionUtils.isNotEmpty(deliveryMtls)) {
                billStatus = BillStatus.PARTICIALSTOCKIN.getId();
            } else {
                billStatus = BillStatus.ALLSTOCKINED.getId();
            }
            if (mtls.get(0).getSourceBillType() == BillType.PURCHASEORDER.getId()) {
                purchase.setBillStatus(billStatus);
                info.setBillStatus(billStatus);
                purchaseService.updateById(purchase);
            } else if (mtls.get(0).getSourceBillType() == BillType.SALERETURNORDER.getId()) {
                saleOrderReturn.setBillStatus(billStatus);
                info.setBillStatus(billStatus);
                saleOrderReturnService.updateById(saleOrderReturn);
            }
            info.setPutInTime(new Date());
            updateById(info);
        }
        return true;
    }

    @Override
    public void deleteBySourceId(String sourceId) {

        InventoryIn exist = getOne(new LambdaQueryWrapper<InventoryIn>().eq(InventoryIn::getSourceId, sourceId).eq(InventoryIn::getRowSts, RowSts.EFFECTIVE.getId()));
        if (null != exist) {
            exist.setRowSts(RowSts.DELETED.getId());
            updateById(exist);

            List<InventoryInMtl> inventoryInMtls = inventoryInMtlService.list(new LambdaQueryWrapper<InventoryInMtl>().eq(InventoryInMtl::getSourceId, exist.getId()));
            if (CollectionUtils.isNotEmpty(inventoryInMtls)) {
                inventoryInMtls.stream().forEach(o->{
                    o.setRowSts(RowSts.DELETED.getId());
                });
                inventoryInMtlService.updateBatchById(inventoryInMtls);
            }
        }
    }
}
