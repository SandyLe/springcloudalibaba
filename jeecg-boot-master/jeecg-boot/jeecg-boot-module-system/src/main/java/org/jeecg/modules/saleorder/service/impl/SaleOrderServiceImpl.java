package org.jeecg.modules.saleorder.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.basic.dto.DeliveryEditDto;
import org.jeecg.modules.inventory.service.InventoryOutService;
import org.jeecg.modules.saleorder.entity.SaleOrder;
import org.jeecg.modules.saleorder.mapper.SaleOrderMapper;
import org.jeecg.modules.saleorder.service.SaleOrderDeliveryInfoService;
import org.jeecg.modules.saleorder.service.SaleOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SaleOrderServiceImpl extends ServiceImpl<SaleOrderMapper, SaleOrder>  implements SaleOrderService {

    @Autowired
    private SaleOrderMapper saleOrderMapper;
    @Lazy
    @Autowired
    private InventoryOutService inventoryOutService;
    @Autowired
    private SaleOrderDeliveryInfoService saleOrderDeliveryInfoService;

    @Transactional
    @Override
    public void delivery(DeliveryEditDto deliveryEditDto) throws Exception {
        /*String billId = deliveryEditDto.getId();
        SaleOrder saleOrder = getById(billId);
        saleOrder.setWarehouseId(deliveryEditDto.getWarehouseId());
        saleOrder.setDeliveryTime(deliveryEditDto.getDeliveryTime());
        saleOrder.setInstallTime(deliveryEditDto.getInstallTime());

        SaleOrderDeliveryInfo cdi = new SaleOrderDeliveryInfo();
        BeanUtils.copyProperties(cdi,deliveryEditDto);
        cdi.setCompanyId(saleOrder.getCompanyId());
        cdi.setCdiSourceId(saleOrder.getCustomerId());
        cdi.setSourceId(saleOrder.getId());
        cdi.setSourceBillCode(saleOrder.getCode());
        cdi.setWarehouseId(saleOrder.getWarehouseId());
        cdi.setId(deliveryEditDto.getCdiId());

        cdi.setCreateBy(deliveryEditDto.getCdiCreateBy());
        cdi.setCreateTime(deliveryEditDto.getCdiCreateTime());
        cdi.setUpdateBy(deliveryEditDto.getCdiUpdateBy());
        cdi.setUpdateTime(deliveryEditDto.getCdiUpdateTime());
        cdi.setCode(deliveryEditDto.getCdiCode());
        cdi.setName(deliveryEditDto.getCdiName());
        cdi.setRowSts(deliveryEditDto.getCdiRowSts());
        cdi.setSort(deliveryEditDto.getCdiSort());
        cdi.setCode(deliveryEditDto.getCdiContent());
        if (StringUtils.isEmpty(cdi.getId())) {
            cdi.setBillStatus(BillStatus.TOSEND.getId());
            saleOrder.setBillStatus(BillStatus.TOSEND.getId());
        }

        // ????????????
        InventoryOut inventoryOut = new InventoryOut(saleOrder.getId(),saleOrder.getCode(), BillType.INVENTORYOUT.getId(), BillType.SALEORDER.getId(), saleOrder.getWarehouseId(), saleOrder.getDeliveryTime(), BillStatus.TOSEND.getId());
        inventoryOut.setRowSts(RowSts.EFFECTIVE.getId());
        inventoryOut.setCompanyId(saleOrder.getCompanyId());
        inventoryOutService.saveToInventoryOut(inventoryOut);
        // ????????????????????????
        updateById(saleOrder);
        saleOrderDeliveryInfoService.saveSaleOrderDelivery(cdi);*/
    }

    @Override
    public SaleOrder getOneByCode(String code, String companyId) {

        LambdaQueryWrapper<SaleOrder> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(SaleOrder::getCode, code);
        lambdaQueryWrapper.eq(SaleOrder::getCompanyId, companyId);
        return super.getOne(lambdaQueryWrapper);
    }
}
