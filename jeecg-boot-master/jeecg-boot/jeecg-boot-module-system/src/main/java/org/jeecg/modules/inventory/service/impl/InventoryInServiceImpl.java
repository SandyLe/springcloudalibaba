package org.jeecg.modules.inventory.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.jeecg.modules.basic.enums.BillType;
import org.jeecg.modules.basic.enums.RowSts;
import org.jeecg.modules.inventory.dto.PreInventoryOutMtl;
import org.jeecg.modules.inventory.entity.InventoryIn;
import org.jeecg.modules.inventory.entity.InventoryInMtl;
import org.jeecg.modules.inventory.mapper.InventoryInMapper;
import org.jeecg.modules.inventory.service.InventoryInMtlService;
import org.jeecg.modules.inventory.service.InventoryInService;
import org.jeecg.modules.purchase.entity.PurchaseMtl;
import org.jeecg.modules.purchase.service.PurchaseMtlService;
import org.jeecg.modules.saleorder.entity.SaleOrderReturnMtl;
import org.jeecg.modules.saleorder.service.SaleOrderReturnMtlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    @Override
    public List<PreInventoryOutMtl> getDeliveryMtlList(String id, String sourceId) {
        return null;
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
                    inventoryInMtls.add(new InventoryInMtl(o.getId(), o.getSourceId(), o.getMtlId(), o.getQuantity(), o.getUnitId(), RowSts.EFFECTIVE.getId()));
                });
            }
        } else if (inventoryIn.getSourceBillType() == BillType.SALERETURNORDER.getId()) {
            LambdaQueryWrapper<SaleOrderReturnMtl> queryWrapper = new LambdaQueryWrapper<SaleOrderReturnMtl>().eq(SaleOrderReturnMtl::getSourceId, inventoryIn.getSourceId());
            List<SaleOrderReturnMtl> saleOrderReturnMtls = saleOrderReturnMtlService.list(queryWrapper);
            if (CollectionUtils.isNotEmpty(saleOrderReturnMtls)) {
                saleOrderReturnMtls.forEach(o ->{
                    inventoryInMtls.add(new InventoryInMtl(o.getId(), o.getSourceId(), o.getMtlId(), o.getQuantity(), o.getUnitId(), RowSts.EFFECTIVE.getId()));
                });
            }
        }
        inventoryInMtlService.saveBatch(inventoryInMtls);
        return inventoryIn.getId();
    }

    @Override
    public Boolean stockIn(List<PreInventoryOutMtl> mtls) {
        return null;
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
