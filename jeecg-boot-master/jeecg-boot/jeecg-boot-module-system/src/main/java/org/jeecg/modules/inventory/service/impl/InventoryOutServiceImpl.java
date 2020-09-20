package org.jeecg.modules.inventory.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.jeecg.common.enums.*;
import org.jeecg.common.util.Constants;
import org.jeecg.modules.basic.service.BillCodeBuilderService;
import org.jeecg.modules.changeorder.entity.ChangeOrder;
import org.jeecg.modules.changeorder.entity.ChangeOrderDtl;
import org.jeecg.modules.changeorder.service.ChangeOrderDtlService;
import org.jeecg.modules.changeorder.service.ChangeOrderService;
import org.jeecg.modules.combind.entity.Assemble;
import org.jeecg.modules.combind.entity.AssembleDtl;
import org.jeecg.modules.combind.entity.Teardown;
import org.jeecg.modules.combind.service.AssembleDtlService;
import org.jeecg.modules.combind.service.AssembleService;
import org.jeecg.modules.combind.service.TeardownDtlService;
import org.jeecg.modules.combind.service.TeardownService;
import org.jeecg.modules.inventory.dto.PreInventoryDto;
import org.jeecg.modules.inventory.entity.*;

import org.jeecg.modules.inventory.mapper.InventoryOutMapper;
import org.jeecg.modules.inventory.service.*;
import org.jeecg.modules.logistics.dto.LogisticsOrderDto;
import org.jeecg.modules.logistics.entity.LogisticsOrderDtl;
import org.jeecg.modules.logistics.service.LogisticsOrderService;
import org.jeecg.modules.purchase.entity.PurchaseReturn;
import org.jeecg.modules.purchase.entity.PurchaseReturnMtl;
import org.jeecg.modules.purchase.service.PurchaseReturnMtlService;
import org.jeecg.modules.inventory.dto.PreInventoryOutMtl;
import org.jeecg.modules.purchase.service.PurchaseReturnService;
import org.jeecg.modules.saleorder.entity.SaleOrder;
import org.jeecg.modules.saleorder.entity.SaleOrderAddress;
import org.jeecg.modules.saleorder.entity.SaleOrderMtl;
import org.jeecg.modules.saleorder.service.SaleOrderAddressService;
import org.jeecg.modules.saleorder.service.SaleOrderMtlService;
import org.jeecg.modules.saleorder.service.SaleOrderService;
import org.jeecg.modules.workorder.entity.WorkOrder;
import org.jeecg.modules.workorder.entity.WorkOrderDtl;
import org.jeecg.modules.workorder.service.WorkOrderDtlService;
import org.jeecg.modules.workorder.service.WorkOrderService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class InventoryOutServiceImpl extends ServiceImpl<InventoryOutMapper, InventoryOut>  implements InventoryOutService {

//    @Autowired
//    private InventoryOutMapper inventoryOutMapper;

    @Autowired
    private SaleOrderMtlService saleOrderMtlService;
    @Autowired
    private PurchaseReturnMtlService iPurchaseReturnMtlService;
    @Autowired
    private InventoryOutMtlService inventoryOutMtlService;
    @Autowired
    private SaleOrderService saleOrderService;
    @Autowired
    private InventoryService inventoryService;
    @Autowired
    private InventoryLogService inventoryLogService;
    @Autowired
    private PurchaseReturnService purchaseReturnService;
    @Autowired
    private AllotDtlService allotDtlService;
    @Autowired
    private AllotService allotService;
    @Autowired
    private AssembleService assembleService;
    @Autowired
    private AssembleDtlService assembleDtlService;
    @Autowired
    private TeardownService teardownService;
    @Autowired
    private TeardownDtlService teardownDtlService;
    @Autowired
    private ChangeOrderDtlService changeOrderDtlService;
    @Autowired
    private ChangeOrderService changeOrderService;
    @Autowired
    private WorkOrderService workOrderService;
    @Autowired
    private WorkOrderDtlService workOrderDtlService;
    @Autowired
    private BillCodeBuilderService billCodeBuilderService;
    @Autowired
    private SaleOrderAddressService saleOrderAddressService;
    @Autowired
    private LogisticsOrderService logisticsOrderService;

    @Override
    @Transactional
    public Boolean stockOut(PreInventoryDto preInventoryDto) {

        List<PreInventoryOutMtl> mtls = preInventoryDto.getMtls();
        InventoryOut info = getById(mtls.get(0).getBillId());
        if (null != info) {

            // 物流单信息
            LogisticsOrderDto orderDto = new LogisticsOrderDto();
            orderDto.setBillType(BillType.LOGISTICSORDER.getId());
            orderDto.setCompanyId(info.getCompanyId());
            orderDto.setBillDate(new Date());
            orderDto.setBillStatus(BillStatus.NEW.getId());
            orderDto.setSourceBillType(info.getSourceBillType());
            orderDto.setSourceId(info.getSourceId());
            orderDto.setSourceCode(info.getSourceCode());
            orderDto.setDeliveryTypeId(preInventoryDto.getDeliveryTypeId());
            orderDto.setLogisticsNo(preInventoryDto.getLogisticsNo());
            orderDto.setLogisticsId(preInventoryDto.getLogisticsId());
            BigDecimal totalQty = BigDecimal.ZERO;
            List<LogisticsOrderDtl> dtls = Lists.newArrayList();

            for (PreInventoryOutMtl mtl : mtls) {
                InventoryLog inventoryLog = new InventoryLog(info.getId(), mtl.getSourceId(), info.getSourceBillType(), mtl.getMtlId(),
                        mtl.getWarehouseId(), null, mtl.getQuantity(), null, mtl.getUnitId(), mtl.getOperationId(), null, info.getCompanyId(),
                        mtl.getAuxiliaryId());
                if (null != preInventoryDto.getDeliveryDate()) {
                    inventoryLog.setOperateTime(preInventoryDto.getDeliveryDate());
                } else {
                    inventoryLog.setOperateTime(new Date());
                }
                inventoryService.updateInventory(inventoryLog);

                if (preInventoryDto.getDeliveryTypeId() > DeliveryType.TakeTheir.getId()) {
                    LogisticsOrderDtl dtl = new LogisticsOrderDtl();
                    BeanUtils.copyProperties(mtl, dtl, "sourceId");
                    dtl.setSourceId(mtl.getSourceId());
                    dtls.add(dtl);
                    totalQty = totalQty.add(mtl.getQuantity());
                }
            }

            if (preInventoryDto.getDeliveryTypeId() > DeliveryType.TakeTheir.getId()) {
                orderDto.setDetaillist(dtls);
                orderDto.setNumber(totalQty);
            }

            List<PreInventoryOutMtl> deliveryMtls = getDeliveryMtlList(info.getId(), info.getSourceId());
            if (mtls.get(0).getSourceBillType() == BillType.SALEORDER.getId()) {
                SaleOrder saleOrder = saleOrderService.getById(info.getSourceId());
                if (CollectionUtils.isNotEmpty(deliveryMtls)) {
                    saleOrder.setBillStatus(BillStatus.PARTICIALSTOCK.getId());
                    info.setBillStatus(BillStatus.PARTICIALSTOCK.getId());
                } else {
                    saleOrder.setBillStatus(BillStatus.STOCKED.getId());
                    info.setBillStatus(BillStatus.STOCKED.getId());
                }
                saleOrder.setDeliveryType(preInventoryDto.getDeliveryTypeId());
                saleOrderService.updateById(saleOrder);

                SaleOrderAddress saleOrderAddress = saleOrderAddressService.findBySouorceId(saleOrder.getId());
                if (null != saleOrderAddress) {
                    BeanUtils.copyProperties(saleOrderAddress, orderDto, "name", "tel", "createTime", "updateTime", "companyId", "id", "createBy", "updateBy");
                    orderDto.setAddress(saleOrderAddress.getName());
                    orderDto.setRecipientsPhone(saleOrderAddress.getTel());
                }
            } else if (mtls.get(0).getSourceBillType() == BillType.PURCHASERETURNORDER.getId()) {
                PurchaseReturn purchaseReturn = purchaseReturnService.getById(info.getSourceId());
                if (CollectionUtils.isNotEmpty(deliveryMtls)) {
                    purchaseReturn.setBillStatus(BillStatus.PARTICIALSTOCK.getId());
                    info.setBillStatus(BillStatus.PARTICIALSTOCK.getId());
                } else {
                    purchaseReturn.setBillStatus(BillStatus.STOCKED.getId());
                    info.setBillStatus(BillStatus.STOCKED.getId());
                }
                purchaseReturnService.updateById(purchaseReturn);
            } else if (mtls.get(0).getSourceBillType() == BillType.ALLOT.getId()) {
                Allot allot = allotService.getById(info.getSourceId());
                if (CollectionUtils.isNotEmpty(deliveryMtls)) {
                    allot.setBillStatus(BillStatus.PARTICIALSTOCK.getId());
                    info.setBillStatus(BillStatus.PARTICIALSTOCK.getId());
                } else {
                    allot.setBillStatus(BillStatus.STOCKED.getId());
                    info.setBillStatus(BillStatus.STOCKED.getId());
                }
                allotService.updateById(allot);
            } else if (mtls.get(0).getSourceBillType() == BillType.ASSEMBLE.getId()) {
                Assemble assemble = assembleService.getById(info.getSourceId());
                if (CollectionUtils.isNotEmpty(deliveryMtls)) {
                    assemble.setBillStatus(BillStatus.PARTICIALSTOCK.getId());
                    info.setBillStatus(BillStatus.PARTICIALSTOCK.getId());
                } else {
                    assemble.setBillStatus(BillStatus.STOCKED.getId());
                    info.setBillStatus(BillStatus.STOCKED.getId());
                }
                assembleService.updateById(assemble);
            } else if (mtls.get(0).getSourceBillType() == BillType.TEARDOWN.getId()) {
                Teardown teardown = teardownService.getById(info.getSourceId());
                if (CollectionUtils.isNotEmpty(deliveryMtls)) {
                    teardown.setBillStatus(BillStatus.PARTICIALSTOCK.getId());
                    info.setBillStatus(BillStatus.PARTICIALSTOCK.getId());
                } else {
                    teardown.setBillStatus(BillStatus.STOCKED.getId());
                    info.setBillStatus(BillStatus.STOCKED.getId());
                }
                teardownService.updateById(teardown);
            } else if (mtls.get(0).getSourceBillType() == BillType.CHANGEORDER.getId()) {
                ChangeOrder changeOrder = changeOrderService.getById(info.getSourceId());
                if (CollectionUtils.isNotEmpty(deliveryMtls)) {
                    changeOrder.setBillStatus(BillStatus.PARTICIALSTOCK.getId());
                    info.setBillStatus(BillStatus.PARTICIALSTOCK.getId());
                } else {
                    changeOrder.setBillStatus(BillStatus.STOCKED.getId());
                    info.setBillStatus(BillStatus.STOCKED.getId());
                }
                changeOrderService.updateById(changeOrder);
            } else if (mtls.get(0).getSourceBillType() == BillType.WORKORDER.getId()) {
                WorkOrder workOrder = workOrderService.getById(info.getSourceId());
                if (CollectionUtils.isNotEmpty(deliveryMtls)) {
                    workOrder.setBillStatus(BillStatus.PARTICIALSTOCK.getId());
                    info.setBillStatus(BillStatus.PARTICIALSTOCK.getId());
                } else {
                    workOrder.setBillStatus(BillStatus.STOCKED.getId());
                    info.setBillStatus(BillStatus.STOCKED.getId());
                }
                workOrderService.updateById(workOrder);
            }
            // 更新发货信息
            updateById(info);
            if (preInventoryDto.getDeliveryTypeId() > DeliveryType.TakeTheir.getId()) {
                logisticsOrderService.saveOrder(orderDto);
            }
        }
        return true;
    }
    @Override
    public String saveToInventoryOut (InventoryOut inventoryOut) {
        if (StringUtils.isEmpty(inventoryOut.getId())) {
            inventoryOut.setCode(billCodeBuilderService.getBillCode(BillType.INVENTORYOUT.getId()));
        }
        InventoryOut existCode = getOne(new LambdaQueryWrapper<InventoryOut>().eq(InventoryOut::getCode, inventoryOut.getCode()).ne(InventoryOut::getId, inventoryOut.getId()));
        Assert.isNull(existCode, "单号已存在！");
        // 保存主表
        save(inventoryOut);

        List<InventoryOutMtl> inventoryOutMtls = Lists.newArrayList();
        if (inventoryOut.getSourceBillType() == BillType.SALEORDER.getId()) {
            LambdaQueryWrapper<SaleOrderMtl> queryWrapper = new LambdaQueryWrapper<SaleOrderMtl>().eq(SaleOrderMtl::getSourceId, inventoryOut.getSourceId());
            List<SaleOrderMtl> saleOrderMtls = saleOrderMtlService.list(queryWrapper);
            if (CollectionUtils.isNotEmpty(saleOrderMtls)) {
                saleOrderMtls.forEach(o ->{
                    inventoryOutMtls.add(new InventoryOutMtl(inventoryOut.getId(), o.getSourceId(), inventoryOut.getSourceBillType(), o.getMtlId(), o.getQuantity(), o.getUnitId(),
                            RowSts.EFFECTIVE.getId(), o.getAuxiliaryId()));
                });
                SaleOrder saleOrder = saleOrderService.getById(inventoryOut.getSourceId());
                saleOrder.setBillStatus(BillStatus.TOSEND.getId());
                saleOrderService.saveOrUpdate(saleOrder);
            }
        } else if (inventoryOut.getSourceBillType() == BillType.PURCHASERETURNORDER.getId()) {
            LambdaQueryWrapper<PurchaseReturnMtl> queryWrapper = new LambdaQueryWrapper<PurchaseReturnMtl>().eq(PurchaseReturnMtl::getSourceId, inventoryOut.getSourceId());
            List<PurchaseReturnMtl> purchaseReturnMtls = iPurchaseReturnMtlService.list(queryWrapper);
            if (CollectionUtils.isNotEmpty(purchaseReturnMtls)) {
                purchaseReturnMtls.forEach(o ->{
                    inventoryOutMtls.add(new InventoryOutMtl(inventoryOut.getId(), o.getSourceId(), inventoryOut.getSourceBillType(), o.getMtlId(), o.getQuantity(), o.getUnitId(),
                            RowSts.EFFECTIVE.getId(), o.getAuxiliaryId()));
                });
            }
        } else if (inventoryOut.getSourceBillType() == BillType.ALLOT.getId()) {
            LambdaQueryWrapper<AllotDtl> queryWrapper = new LambdaQueryWrapper<AllotDtl>().eq(AllotDtl::getSourceId, inventoryOut.getSourceId());
            List<AllotDtl> allotDtls = allotDtlService.list(queryWrapper);
            if (CollectionUtils.isNotEmpty(allotDtls)) {
                allotDtls.forEach(o ->{
                    inventoryOutMtls.add(new InventoryOutMtl(inventoryOut.getId(), o.getSourceId(), inventoryOut.getSourceBillType(), o.getMtlId(), o.getAllotAmount(), o.getUnitId(),
                            RowSts.EFFECTIVE.getId(), o.getAuxiliaryId()));
                });
            }
        } else if (inventoryOut.getSourceBillType() == BillType.ASSEMBLE.getId()) {
            LambdaQueryWrapper<AssembleDtl> queryWrapper = new LambdaQueryWrapper<AssembleDtl>().eq(AssembleDtl::getSourceId, inventoryOut.getSourceId());
            List<AssembleDtl> assembleDtls = assembleDtlService.list(queryWrapper);
            if (CollectionUtils.isNotEmpty(assembleDtls)) {
                assembleDtls.forEach(o ->{
                    inventoryOutMtls.add(new InventoryOutMtl(inventoryOut.getId(), o.getSourceId(), inventoryOut.getSourceBillType(), o.getMtlId(), o.getQuantity(), o.getUnitId(),
                            RowSts.EFFECTIVE.getId(), o.getAuxiliaryId()));
                });
            }
        } else if (inventoryOut.getSourceBillType() == BillType.TEARDOWN.getId()) {
            Teardown teardown = teardownService.getById(inventoryOut.getSourceId());
            inventoryOutMtls.add(new InventoryOutMtl(inventoryOut.getId(), teardown.getId(), inventoryOut.getSourceBillType(), teardown.getMtlId(), teardown.getQuantity(), teardown.getUnitId(),
                    RowSts.EFFECTIVE.getId(), teardown.getAuxiliaryId()));
        } else if (inventoryOut.getSourceBillType() == BillType.CHANGEORDER.getId()) {
            LambdaQueryWrapper<ChangeOrderDtl> queryWrapper = new LambdaQueryWrapper<ChangeOrderDtl>().eq(ChangeOrderDtl::getSourceId, inventoryOut.getSourceId());
            List<ChangeOrderDtl> allotDtls = changeOrderDtlService.list(queryWrapper);
            if (CollectionUtils.isNotEmpty(allotDtls)) {
                allotDtls.forEach(o ->{
                    inventoryOutMtls.add(new InventoryOutMtl(inventoryOut.getId(), o.getSourceId(), inventoryOut.getSourceBillType(), o.getNewMtlId(), o.getNewQuantity(), o.getNewUnitId(),
                            RowSts.EFFECTIVE.getId(), o.getAuxiliaryId()));
                });
            }
        } else if (inventoryOut.getSourceBillType() == BillType.WORKORDER.getId()) {
            LambdaQueryWrapper<WorkOrderDtl> queryWrapper = new LambdaQueryWrapper<WorkOrderDtl>().eq(WorkOrderDtl::getSourceId, inventoryOut.getSourceId());
            List<WorkOrderDtl> workOrderDtls = workOrderDtlService.list(queryWrapper);
            if (CollectionUtils.isNotEmpty(workOrderDtls)) {
                workOrderDtls.forEach(o ->{
                    inventoryOutMtls.add(new InventoryOutMtl(inventoryOut.getId(), o.getSourceId(), inventoryOut.getSourceBillType(), o.getMtlId(), o.getQuantity(), o.getUnitId(),
                            RowSts.EFFECTIVE.getId(), o.getAuxiliaryId()));
                });
            }
        }
        inventoryOutMtlService.saveBatch(inventoryOutMtls);
        return inventoryOut.getId();
    }

    @Override
    public List<PreInventoryOutMtl> getDeliveryMtlList(String id, String sourceId) {
        List<PreInventoryOutMtl> preInventoryOutMtls = Lists.newArrayList();
        InventoryOut inventoryOut = getById(id);
        Integer operationId = Constants.OUTOPERATIONS.get(inventoryOut.getSourceBillType());
        LambdaQueryWrapper<InventoryOutMtl> querySaleMtlWrapper = new QueryWrapper<InventoryOutMtl>().lambda().eq(InventoryOutMtl::getSourceBillId, sourceId).eq(InventoryOutMtl::getRowSts, RowSts.EFFECTIVE.getId());
        LambdaQueryWrapper<InventoryLog> queryInventoryLogWrapper = new QueryWrapper<InventoryLog>().lambda().eq(InventoryLog::getSourceId, id);
        queryInventoryLogWrapper.eq(InventoryLog::getOperationId, operationId);
        List<InventoryOutMtl> inventoryOutMtls = inventoryOutMtlService.list(querySaleMtlWrapper);
        List<InventoryLog> inventoryLogs = inventoryLogService.list(queryInventoryLogWrapper);
        Map<String, BigDecimal> mtlDeliveryQtyMap = new HashMap<>();
        inventoryLogs.stream().forEach(o->{
            mtlDeliveryQtyMap.put(o.getMtlId(), null == mtlDeliveryQtyMap.get(o.getMtlId()) ? o.getOptAmount() : mtlDeliveryQtyMap.get(o.getMtlId()).add(o.getOptAmount()));
        });
        inventoryOutMtls.forEach(o->{
            BigDecimal tempAmout = o.getQuantity();
            if (null != mtlDeliveryQtyMap.get(o.getMtlId())) {
                tempAmout = tempAmout.subtract(mtlDeliveryQtyMap.get(o.getMtlId()));
            }
            if (tempAmout.compareTo(BigDecimal.ZERO) > 0 ) {
                PreInventoryOutMtl preInventoryOutMtl = new PreInventoryOutMtl();
                preInventoryOutMtl.setBillId(id);
                preInventoryOutMtl.setSourceId(sourceId);
                preInventoryOutMtl.setMtlId(o.getMtlId());
                preInventoryOutMtl.setSourceBillType(o.getSourceBillType());
                preInventoryOutMtl.setQuantity(tempAmout);
                preInventoryOutMtl.setUnitId(o.getUnitId());
                preInventoryOutMtl.setOperationId(operationId);
                preInventoryOutMtl.setWarehouseId(inventoryOut.getWarehouseId());
                preInventoryOutMtl.setAuxiliaryId(o.getAuxiliaryId());
                preInventoryOutMtls.add(preInventoryOutMtl);
            }
        });
        return preInventoryOutMtls;
    }

    @Override
    public InventoryOut queryBySourceId(Integer sourceBillType, String sourceId) {

        LambdaQueryWrapper<InventoryOut> lambdaQueryWrapper = new LambdaQueryWrapper<InventoryOut>();
        lambdaQueryWrapper.eq(InventoryOut::getSourceBillType, sourceBillType);
        lambdaQueryWrapper.eq(InventoryOut::getSourceId, sourceId);
        lambdaQueryWrapper.eq(InventoryOut::getRowSts, RowSts.EFFECTIVE.getId());

        return super.getOne(lambdaQueryWrapper);
    }

    @Override
    public void deleteBySourceId(Integer sourceBillType, String sourceId) {

        InventoryOut exist = queryBySourceId(sourceBillType, sourceId);
        if (null != exist) {
            exist.setRowSts(RowSts.DELETED.getId());
            updateById(exist);

            List<InventoryOutMtl> inventoryInMtls = inventoryOutMtlService.list(new LambdaQueryWrapper<InventoryOutMtl>().eq(InventoryOutMtl::getSourceId, exist.getId()));
            if (CollectionUtils.isNotEmpty(inventoryInMtls)) {
                inventoryInMtls.stream().forEach(o->{
                    o.setRowSts(RowSts.DELETED.getId());
                });
                inventoryOutMtlService.updateBatchById(inventoryInMtls);
            }
        }
    }
}
