package org.jeecg.modules.inventory.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.jeecg.modules.basic.enums.BillStatus;
import org.jeecg.modules.basic.enums.BillType;
import org.jeecg.modules.basic.enums.InventoryOperation;
import org.jeecg.modules.inventory.entity.InventoryLog;
import org.jeecg.modules.inventory.entity.InventoryOut;

import org.jeecg.modules.inventory.entity.InventoryOutMtl;
import org.jeecg.modules.inventory.mapper.InventoryOutMapper;
import org.jeecg.modules.inventory.service.InventoryLogService;
import org.jeecg.modules.inventory.service.InventoryOutMtlService;
import org.jeecg.modules.inventory.service.InventoryOutService;
import org.jeecg.modules.inventory.service.InventoryService;
import org.jeecg.modules.purchase.entity.PurchaseReturnMtl;
import org.jeecg.modules.purchase.service.PurchaseReturnMtlService;
import org.jeecg.modules.inventory.dto.PreInventoryOutMtl;
import org.jeecg.modules.saleorder.entity.SaleOrder;
import org.jeecg.modules.saleorder.entity.SaleOrderMtl;
import org.jeecg.modules.saleorder.service.SaleOrderMtlService;
import org.jeecg.modules.saleorder.service.SaleOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
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

    @Override
    @Transactional
    public Boolean stockOut(List<PreInventoryOutMtl> mtls) {

        InventoryOut info = getById(mtls.get(0).getBillId());
        if (null != info) {
            for (PreInventoryOutMtl mtl : mtls) {
                InventoryLog inventoryLog = new InventoryLog(mtl.getSourceId(), BillType.SALEORDER.getId(), mtl.getMtlId(),
                        info.getWarehouseId(), null, mtl.getQuantity(), null, mtl.getUnitId(), InventoryOperation.SALEOUT.getId());
                inventoryService.updateInventory(inventoryLog);
            }
            List<PreInventoryOutMtl> deliveryMtls = getDeliveryMtlList(info.getId(), info.getSourceId());
            SaleOrder saleOrder = saleOrderService.getById(info.getSourceId());
            if (CollectionUtils.isNotEmpty(deliveryMtls)) {
                saleOrder.setBillStatus(BillStatus.PARTICIALSTOCK.getId());
                info.setBillStatus(BillStatus.PARTICIALSTOCK.getId());
            } else {
                saleOrder.setBillStatus(BillStatus.STOCKED.getId());
                info.setBillStatus(BillStatus.STOCKED.getId());
            }
            saleOrderService.updateById(saleOrder);
            updateById(info);
        }
        return true;
    }
    @Override
    public String saveToInventoryOut (InventoryOut inventoryOut) {

        // 保存主表
        save(inventoryOut);

        List<InventoryOutMtl> inventoryOutMtls = Lists.newArrayList();
        if (inventoryOut.getSourceBillType() == BillType.SALEORDER.getId()) {
            LambdaQueryWrapper<SaleOrderMtl> queryWrapper = new LambdaQueryWrapper<SaleOrderMtl>().eq(SaleOrderMtl::getSourceId, inventoryOut.getSourceId());
            List<SaleOrderMtl> saleOrderMtls = saleOrderMtlService.list(queryWrapper);
            if (CollectionUtils.isNotEmpty(saleOrderMtls)) {
                saleOrderMtls.forEach(o ->{
                    inventoryOutMtls.add(new InventoryOutMtl(o.getId(), o.getSourceId(), o.getMtlId(), o.getQuantity(), o.getUnitId()));
                });
            }
        } else if (inventoryOut.getSourceBillType() == BillType.PURCHASERETURNORDER.getId()) {
            LambdaQueryWrapper<PurchaseReturnMtl> queryWrapper = new LambdaQueryWrapper<PurchaseReturnMtl>().eq(PurchaseReturnMtl::getSourceId, inventoryOut.getSourceId());
            List<PurchaseReturnMtl> purchaseReturnMtls = iPurchaseReturnMtlService.list(queryWrapper);
            if (CollectionUtils.isNotEmpty(purchaseReturnMtls)) {
                purchaseReturnMtls.forEach(o ->{
                    inventoryOutMtls.add(new InventoryOutMtl(o.getId(), o.getSourceId(), o.getMtlId(), o.getQuantity(), o.getUnitId()));
                });
            }
        }
        inventoryOutMtlService.saveBatch(inventoryOutMtls);
        return inventoryOut.getId();
    }

    @Override
    public List<PreInventoryOutMtl> getDeliveryMtlList(String id, String sourceId) {
        List<PreInventoryOutMtl> preInventoryOutMtls = Lists.newArrayList();
        LambdaQueryWrapper<InventoryOutMtl> querySaleMtlWrapper = new QueryWrapper<InventoryOutMtl>().lambda().eq(InventoryOutMtl::getSourceId, id);
        LambdaQueryWrapper<InventoryLog> queryInventoryLogWrapper = new QueryWrapper<InventoryLog>().lambda().eq(InventoryLog::getSourceId, sourceId);
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
                preInventoryOutMtl.setQuantity(tempAmout);
                preInventoryOutMtl.setUnitId(o.getUnitId());
                preInventoryOutMtls.add(preInventoryOutMtl);
            }
        });
        return preInventoryOutMtls;
    }
}
