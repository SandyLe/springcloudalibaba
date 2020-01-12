package org.jeecg.modules.saleorder.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.jeecg.modules.basic.enums.BillStatus;
import org.jeecg.modules.basic.enums.BillType;
import org.jeecg.modules.basic.enums.InventoryOperation;
import org.jeecg.modules.inventory.entity.InventoryLog;
import org.jeecg.modules.inventory.service.InventoryLogService;
import org.jeecg.modules.inventory.service.InventoryService;
import org.jeecg.modules.saleorder.dto.SaleOrderDeliveryMtl;
import org.jeecg.modules.saleorder.entity.SaleOrder;
import org.jeecg.modules.saleorder.entity.SaleOrderDeliveryInfo;
import org.jeecg.modules.saleorder.entity.SaleOrderMtl;
import org.jeecg.modules.saleorder.mapper.SaleOrderDeliveryInfoMapper;
import org.jeecg.modules.saleorder.service.SaleOrderDeliveryInfoService;
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
public class SaleOrderDeliveryInfoServiceImpl extends ServiceImpl<SaleOrderDeliveryInfoMapper, SaleOrderDeliveryInfo> implements SaleOrderDeliveryInfoService{

    @Autowired
    private SaleOrderDeliveryInfoMapper saleOrderDeliveryInfoMapper;

    @Autowired
    private SaleOrderMtlService saleOrderMtlService;
    @Autowired
    private SaleOrderService saleOrderService;
    @Autowired
    private InventoryService inventoryService;
    @Autowired
    private InventoryLogService inventoryLogService;

    @Override
    public List<SaleOrderDeliveryMtl> getDeliveryMtlList(String id, String sourceId) {
        List<SaleOrderDeliveryMtl> saleOrderDeliveryMtls = Lists.newArrayList();
        LambdaQueryWrapper<SaleOrderMtl> querySaleMtlWrapper = new QueryWrapper<SaleOrderMtl>().lambda().eq(SaleOrderMtl::getSourceId, sourceId);
        LambdaQueryWrapper<InventoryLog> queryInventoryLogWrapper = new QueryWrapper<InventoryLog>().lambda().eq(InventoryLog::getSourceId, sourceId);
        List<SaleOrderMtl> saleOrderMtls = saleOrderMtlService.list(querySaleMtlWrapper);
        List<InventoryLog> inventoryLogs = inventoryLogService.list(queryInventoryLogWrapper);
        Map<String, BigDecimal> mtlDeliveryQtyMap = new HashMap<>();
        inventoryLogs.stream().forEach(o->{
            mtlDeliveryQtyMap.put(o.getMtlId(), null == mtlDeliveryQtyMap.get(o.getMtlId()) ? o.getOptAmount() : mtlDeliveryQtyMap.get(o.getMtlId()).add(o.getOptAmount()));
        });
        saleOrderMtls.forEach(o->{
            BigDecimal tempAmout = o.getAmount();
            if (null != mtlDeliveryQtyMap.get(o.getMtlId())) {
                tempAmout = tempAmout.subtract(mtlDeliveryQtyMap.get(o.getMtlId()));
            }
            if (tempAmout.compareTo(BigDecimal.ZERO) > 0 ) {
                SaleOrderDeliveryMtl saleOrderDeliveryMtl = new SaleOrderDeliveryMtl();
                saleOrderDeliveryMtl.setBillId(id);
                saleOrderDeliveryMtl.setSourceId(sourceId);
                saleOrderDeliveryMtl.setMtlId(o.getMtlId());
                saleOrderDeliveryMtl.setQuantity(tempAmout);
                saleOrderDeliveryMtl.setUnitId(o.getUnitId());
                saleOrderDeliveryMtls.add(saleOrderDeliveryMtl);
            }
        });
        return saleOrderDeliveryMtls;
    }

    @Override
    @Transactional
    public Boolean stockOut(List<SaleOrderDeliveryMtl> mtls) {

        SaleOrderDeliveryInfo info = getById(mtls.get(0).getBillId());
        if (null != info) {
            for (SaleOrderDeliveryMtl mtl : mtls) {
                InventoryLog inventoryLog = new InventoryLog(mtl.getSourceId(), BillType.SALEORDER.getId(), mtl.getMtlId(),
                        info.getWarehouseId(), null, mtl.getQuantity(), null, mtl.getUnitId(), InventoryOperation.SALEOUT.getId());
                inventoryService.updateInventory(inventoryLog);
            }
            List<SaleOrderDeliveryMtl> deliveryMtls = getDeliveryMtlList(info.getId(), info.getSourceId());
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
}
