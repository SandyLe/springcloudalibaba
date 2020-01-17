package org.jeecg.modules.inventory.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.jeecg.modules.basic.enums.BillType;
import org.jeecg.modules.inventory.entity.InventoryOut;
import org.jeecg.modules.inventory.entity.InventoryOutMtl;
import org.jeecg.modules.inventory.mapper.InventoryOutMapper;
import org.jeecg.modules.inventory.service.InventoryOutMtlService;
import org.jeecg.modules.inventory.service.InventoryOutService;
import org.jeecg.modules.purchase.entity.PurchaseReturnDtl;
import org.jeecg.modules.purchase.service.IPurchaseReturnDtlService;
import org.jeecg.modules.saleorder.entity.SaleOrderMtl;
import org.jeecg.modules.saleorder.service.SaleOrderMtlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InventoryOutServiceImpl extends ServiceImpl<InventoryOutMapper, InventoryOut>  implements InventoryOutService {

//    @Autowired
//    private InventoryOutMapper inventoryOutMapper;

    @Autowired
    private SaleOrderMtlService saleOrderMtlService;
    @Autowired
    private IPurchaseReturnDtlService iPurchaseReturnMtlService;
    @Autowired
    private InventoryOutMtlService inventoryOutMtlService;

    @Override
    public String saveToInventoryOut (InventoryOut inventoryOut) {

        List<InventoryOutMtl> inventoryOutMtls = Lists.newArrayList();
        if (inventoryOut.getBillType() == BillType.SALEORDER.getId()) {
            LambdaQueryWrapper<SaleOrderMtl> queryWrapper = new LambdaQueryWrapper<SaleOrderMtl>().eq(SaleOrderMtl::getSourceId, inventoryOut.getSourceId());
            List<SaleOrderMtl> saleOrderMtls = saleOrderMtlService.list(queryWrapper);
            if (CollectionUtils.isNotEmpty(saleOrderMtls)) {
                saleOrderMtls.forEach(o ->{
                    inventoryOutMtls.add(new InventoryOutMtl(o.getSourceId(), o.getMtlId(), o.getQuantity().toString(), o.getUnitId()));
                });
            }
        } else if (inventoryOut.getBillType() == BillType.PURCHASERETURNORDER.getId()) {
            LambdaQueryWrapper<PurchaseReturnDtl> queryWrapper = new LambdaQueryWrapper<PurchaseReturnDtl>().eq(PurchaseReturnDtl::getSourceId, inventoryOut.getSourceId());
            List<PurchaseReturnDtl> purchaseReturnMtls = iPurchaseReturnMtlService.list(queryWrapper);
            if (CollectionUtils.isNotEmpty(purchaseReturnMtls)) {
                purchaseReturnMtls.forEach(o ->{
                    inventoryOutMtls.add(new InventoryOutMtl(o.getSourceId(), o.getMtlId(), o.getQuantity(), o.getUnitId()));
                });
            }
        }
        inventoryOutMtlService.saveBatch(inventoryOutMtls);
        save(inventoryOut);
        return inventoryOut.getId();
    }

}
