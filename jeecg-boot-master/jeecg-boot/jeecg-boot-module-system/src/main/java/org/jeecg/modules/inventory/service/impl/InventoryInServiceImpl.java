package org.jeecg.modules.inventory.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.jeecg.common.enums.BillStatus;
import org.jeecg.common.enums.BillType;
import org.jeecg.common.enums.InventoryOperation;
import org.jeecg.common.enums.RowSts;
import org.jeecg.common.exception.JeecgBootException;
import org.jeecg.common.util.Constants;
import org.jeecg.modules.basic.service.BillCodeBuilderService;
import org.jeecg.modules.changeorder.entity.ChangeOrder;
import org.jeecg.modules.changeorder.entity.ChangeOrderDtl;
import org.jeecg.modules.changeorder.service.ChangeOrderDtlService;
import org.jeecg.modules.changeorder.service.ChangeOrderService;
import org.jeecg.modules.combind.entity.Assemble;
import org.jeecg.modules.combind.entity.Teardown;
import org.jeecg.modules.combind.entity.TeardownDtl;
import org.jeecg.modules.combind.service.AssembleService;
import org.jeecg.modules.combind.service.TeardownDtlService;
import org.jeecg.modules.combind.service.TeardownService;
import org.jeecg.modules.cost.entity.PurchaseCost;
import org.jeecg.modules.cost.service.PurchaseCostService;
import org.jeecg.modules.inventory.dto.PreInventoryOutMtl;
import org.jeecg.modules.inventory.entity.*;
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
    @Autowired
    private AllotService allotService;
    @Autowired
    private AssembleService assembleService;
    @Autowired
    private TeardownService teardownService;
    @Autowired
    private TeardownDtlService teardownDtlService;
    @Autowired
    private ChangeOrderService changeOrderService;
    @Autowired
    private ChangeOrderDtlService changeOrderDtlService;
    @Autowired
    private BillCodeBuilderService billCodeBuilderService;

    @Override
    public List<PreInventoryOutMtl> getDeliveryMtlList(String id, String sourceId) {
        InventoryIn inventoryIn = getById(id);
        Integer operationId = Constants.INOPERATIONS.get(inventoryIn.getSourceBillType());
        List<PreInventoryOutMtl> preInventoryOutMtls = Lists.newArrayList();
        LambdaQueryWrapper<InventoryInMtl> querySaleMtlWrapper = new QueryWrapper<InventoryInMtl>().lambda().eq(InventoryInMtl::getSourceBillId, sourceId).eq(InventoryInMtl::getRowSts, RowSts.EFFECTIVE.getId());
        LambdaQueryWrapper<InventoryLog> queryInventoryLogWrapper = new QueryWrapper<InventoryLog>().lambda().eq(InventoryLog::getSourceId, id);
        List<InventoryInMtl> inventoryInMtls = inventoryInMtlService.list(querySaleMtlWrapper);
        List<InventoryLog> inventoryLogs = inventoryLogService.list(queryInventoryLogWrapper);
        Map<String, BigDecimal> mtlDeliveryQtyMap = new HashMap<>();
        inventoryLogs.stream().forEach(o -> {
            mtlDeliveryQtyMap.put(o.getMtlId(), null == mtlDeliveryQtyMap.get(o.getMtlId()) ? o.getOptAmount() : mtlDeliveryQtyMap.get(o.getMtlId()).add(o.getOptAmount()));
        });
        inventoryInMtls.forEach(o -> {
            BigDecimal tempAmout = o.getQuantity();
            if (null != mtlDeliveryQtyMap.get(o.getMtlId())) {
                tempAmout = tempAmout.subtract(mtlDeliveryQtyMap.get(o.getMtlId()));
            }
            if (tempAmout.compareTo(BigDecimal.ZERO) > 0) {
                PreInventoryOutMtl preInventoryOutMtl = new PreInventoryOutMtl();
                preInventoryOutMtl.setBillId(id);
                preInventoryOutMtl.setSourceId(sourceId);
                preInventoryOutMtl.setSourceBillType(o.getSourceBillType());
                preInventoryOutMtl.setMtlId(o.getMtlId());
                preInventoryOutMtl.setQuantity(tempAmout);
                preInventoryOutMtl.setUnitId(o.getUnitId());
                preInventoryOutMtl.setOperationId(operationId);
                preInventoryOutMtl.setWarehouseId(inventoryIn.getWarehouseId());
                preInventoryOutMtls.add(preInventoryOutMtl);
            }
        });
        return preInventoryOutMtls;
    }

    @Override
    public String saveToInventoryIn(InventoryIn inventoryIn) {

        // 保存主表
        if (StringUtils.isEmpty(inventoryIn.getId())) {
            inventoryIn.setCode(billCodeBuilderService.getBillCode(BillType.INVENTORYIN.getId()));
        }
        save(inventoryIn);

        // 保存子表
        List<InventoryInMtl> inventoryInMtls = Lists.newArrayList();
        if (inventoryIn.getSourceBillType() == BillType.PURCHASEORDER.getId()) {
            LambdaQueryWrapper<PurchaseMtl> queryWrapper = new LambdaQueryWrapper<PurchaseMtl>().eq(PurchaseMtl::getSourceId, inventoryIn.getSourceId());
            List<PurchaseMtl> purchaseDtls = purchaseMtlService.list(queryWrapper);
            if (CollectionUtils.isNotEmpty(purchaseDtls)) {
                purchaseDtls.forEach(o -> {
                    inventoryInMtls.add(new InventoryInMtl(inventoryIn.getId(), o.getSourceId(), inventoryIn.getSourceBillType(), o.getMtlId(), o.getQuantity(), o.getUnitId(), RowSts.EFFECTIVE.getId()));
                });
            }
        } else if (inventoryIn.getSourceBillType() == BillType.SALERETURNORDER.getId()) {
            LambdaQueryWrapper<SaleOrderReturnMtl> queryWrapper = new LambdaQueryWrapper<SaleOrderReturnMtl>().eq(SaleOrderReturnMtl::getSourceId, inventoryIn.getSourceId());
            List<SaleOrderReturnMtl> saleOrderReturnMtls = saleOrderReturnMtlService.list(queryWrapper);
            if (CollectionUtils.isNotEmpty(saleOrderReturnMtls)) {
                saleOrderReturnMtls.forEach(o -> {
                    inventoryInMtls.add(new InventoryInMtl(inventoryIn.getId(), o.getSourceId(), inventoryIn.getSourceBillType(), o.getMtlId(), o.getQuantity(), o.getUnitId(), RowSts.EFFECTIVE.getId()));
                });
            }
        } else if (inventoryIn.getSourceBillType() == BillType.ALLOT.getId()) {
            LambdaQueryWrapper<AllotDtl> queryWrapper = new LambdaQueryWrapper<AllotDtl>().eq(AllotDtl::getSourceId, inventoryIn.getSourceId());
            List<AllotDtl> allotDtls = allotDtlService.list(queryWrapper);
            if (CollectionUtils.isNotEmpty(allotDtls)) {
                allotDtls.forEach(o -> {
                    inventoryInMtls.add(new InventoryInMtl(inventoryIn.getId(), o.getSourceId(), inventoryIn.getSourceBillType(), o.getMtlId(), o.getAllotAmount(), o.getUnitId(), RowSts.EFFECTIVE.getId()));
                });
            }
        } else if (inventoryIn.getSourceBillType() == BillType.ASSEMBLE.getId()) {
            Assemble assemble = assembleService.getById(inventoryIn.getSourceId());
            if (null != assemble) {
                inventoryInMtls.add(new InventoryInMtl(inventoryIn.getId(), assemble.getId(), inventoryIn.getSourceBillType(), assemble.getMtlId(), assemble.getQuantity(), assemble.getUnitId(), RowSts.EFFECTIVE.getId()));
            }
        } else if (inventoryIn.getSourceBillType() == BillType.TEARDOWN.getId()) {
            LambdaQueryWrapper<TeardownDtl> queryWrapper = new LambdaQueryWrapper<TeardownDtl>().eq(TeardownDtl::getSourceId, inventoryIn.getSourceId());
            List<TeardownDtl> teardownDtls = teardownDtlService.list(queryWrapper);
            if (CollectionUtils.isNotEmpty(teardownDtls)) {
                teardownDtls.forEach(o -> {
                    inventoryInMtls.add(new InventoryInMtl(inventoryIn.getId(), o.getSourceId(), inventoryIn.getSourceBillType(), o.getMtlId(), o.getQuantity(), o.getUnitId(), RowSts.EFFECTIVE.getId()));
                });
            }
        } else if (inventoryIn.getSourceBillType() == BillType.CHANGEORDER.getId()) {
            LambdaQueryWrapper<ChangeOrderDtl> queryWrapper = new LambdaQueryWrapper<ChangeOrderDtl>().eq(ChangeOrderDtl::getSourceId, inventoryIn.getSourceId());
            List<ChangeOrderDtl> changeOrderDtls = changeOrderDtlService.list(queryWrapper);
            if (CollectionUtils.isNotEmpty(changeOrderDtls)) {
                changeOrderDtls.forEach(o -> {
                    inventoryInMtls.add(new InventoryInMtl(inventoryIn.getId(), o.getSourceId(), inventoryIn.getSourceBillType(), o.getMtlId(), o.getQuantity(), o.getUnitId(), RowSts.EFFECTIVE.getId()));
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
            String companyId = null;
            SaleOrderReturn saleOrderReturn = null;
            Purchase purchase = null;
            Allot allot = null;
            Assemble assemble = null;
            Teardown teardown = null;
            ChangeOrder changeOrder = null;
            String batchNo = null;
            Integer billStatus = BillStatus.ALLSTOCKINED.getId();
            if (billType == BillType.PURCHASEORDER.getId()) {
                purchase = purchaseService.getById(info.getSourceId());
                companyId = purchase.getCompanyId();
                batchNo = purchase.getBatchNo();
            } else if (billType == BillType.ALLOT.getId()) {
                allot = allotService.getById(info.getSourceId());
                companyId = allot.getCompanyId();
            } else if (billType == BillType.ASSEMBLE.getId()) {
                assemble = assembleService.getById(info.getSourceId());
                companyId = assemble.getCompanyId();
            } else if (billType == BillType.TEARDOWN.getId()) {
                teardown = teardownService.getById(info.getSourceId());
                companyId = teardown.getCompanyId();
            } else if (billType == BillType.CHANGEORDER.getId()) {
                changeOrder = changeOrderService.getById(info.getSourceId());
                companyId = changeOrder.getCompanyId();
            } else if (billType == BillType.SALERETURNORDER.getId()) {
                saleOrderReturn = saleOrderReturnService.getById(info.getSourceId());
                companyId = saleOrderReturn.getCompanyId();
            }
            for (PreInventoryOutMtl mtl : mtls) {
                // 入库更新库存
                InventoryLog inventoryLog = new InventoryLog(info.getId(), mtl.getSourceId(), info.getSourceBillType(), mtl.getMtlId(),
                        info.getWarehouseId(), null, mtl.getQuantity(), null, mtl.getUnitId(), mtl.getOperationId(), batchNo, companyId);
                inventoryService.updateInventory(inventoryLog);

                // 更新采购产品批次平均价格
                if (billType == BillType.PURCHASEORDER.getId()) {
                    if (StringUtils.isNotBlank(batchNo)) {

                        List<PurchaseMtl> purchaseMtls = purchaseMtlService.findPurchaseMtls(batchNo, mtl.getMtlId());
                        purchaseMtls = purchaseMtls.stream().filter(o -> o.getUnitId().equals(mtl.getUnitId())).collect(Collectors.toList());
                        if (CollectionUtils.isNotEmpty(purchaseMtls)) {
                            BigDecimal totalAmount = BigDecimal.ZERO;
                            BigDecimal totalQty = BigDecimal.ZERO;
                            for (PurchaseMtl purchaseMtl : purchaseMtls) {
                                totalAmount = totalAmount.add(purchaseMtl.getAmount());
                                totalQty = totalQty.add(purchaseMtl.getQuantity());
                            }
                            PurchaseCost purchaseCost = purchaseCostService.findMtlPurchaseCost(mtl.getMtlId(), mtl.getUnitId(), batchNo);
                            if (null == purchaseCost) {
                                purchaseCost = new PurchaseCost();
                                purchaseCost.setBatchNo(batchNo);
                                purchaseCost.setMtlId(mtl.getMtlId());
                                purchaseCost.setUnitId(mtl.getUnitId());
                                purchaseCost.setCompanyId(companyId);
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
                purchaseService.updateById(purchase);
            } else if (mtls.get(0).getSourceBillType() == BillType.SALERETURNORDER.getId()) {
                saleOrderReturn.setBillStatus(billStatus);
                saleOrderReturnService.updateById(saleOrderReturn);
            } else if (billType == BillType.ALLOT.getId()) {
                if (allot.getBillStatus() != BillStatus.STOCKED.getId()) {
                    throw new JeecgBootException("请先完成产品出库操作！");
                }
                allot.setBillStatus(billStatus);
                allotService.updateById(allot);
            } else if (billType == BillType.ASSEMBLE.getId()) {
                if (assemble.getBillStatus() != BillStatus.STOCKED.getId()) {
                    throw new JeecgBootException("请先完成产品出库操作！");
                }
                assemble.setBillStatus(billStatus);
                assembleService.updateById(assemble);
            } else if (billType == BillType.TEARDOWN.getId()) {
                if (teardown.getBillStatus() != BillStatus.STOCKED.getId()) {
                    throw new JeecgBootException("请先完成产品出库操作！");
                }
                teardown.setBillStatus(billStatus);
                teardownService.updateById(teardown);
            } else if (billType == BillType.CHANGEORDER.getId()) {
                changeOrder.setBillStatus(billStatus);
                changeOrderService.updateById(changeOrder);
            }
            info.setBillStatus(billStatus);
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
                inventoryInMtls.stream().forEach(o -> {
                    o.setRowSts(RowSts.DELETED.getId());
                });
                inventoryInMtlService.updateBatchById(inventoryInMtls);
            }
        }
    }
}
