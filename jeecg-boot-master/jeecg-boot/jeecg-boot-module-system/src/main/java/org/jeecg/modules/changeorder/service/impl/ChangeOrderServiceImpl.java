package org.jeecg.modules.changeorder.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.jeecg.common.enums.BillStatus;
import org.jeecg.common.enums.BillType;
import org.jeecg.common.enums.RowSts;
import org.jeecg.modules.basic.service.BillCodeBuilderService;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private ChangeOrderMapper changeOrderMapper;
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
            List<ChangeOrderDtl> mtls = changeOrderdto.getDetaillist().stream().filter(o-> StringUtils.isNotBlank(o.getMtlId())).collect(Collectors.toList());
            mtls.stream().forEach(o->{
                //组装商品详情
                o.setCode(code);
                o.setSourceId(changeOrderdto.getId());
            });
            changeOrderDtlService.saveBatch(mtls);
        }

        if (StringUtils.isNotBlank(changeOrderdto.getWarehouseId())) {
            // 配件出库
            InventoryOut inventoryOut = new InventoryOut(changeOrderdto.getId(), changeOrderdto.getCode(), BillType.STOREOUT.getId(), BillType.CHANGEORDER.getId(), changeOrderdto.getWarehouseId(), new Date(), BillStatus.TOSTOCKOUT.getId());
            inventoryOut.setRowSts(RowSts.EFFECTIVE.getId());
            inventoryOutService.saveToInventoryOut(inventoryOut);
            // 入库单主表
            InventoryIn inventoryIn = new InventoryIn();
            inventoryIn.setBillStatus(BillStatus.TOSTOCKIN.getId());
            inventoryIn.setWarehouseId(changeOrderdto.getWarehouseId());
            inventoryIn.setPutInTime(new Date());
            inventoryIn.setSourceCode(code);
            inventoryIn.setSourceId(changeOrderdto.getId());
            inventoryIn.setBillType(BillType.STOREIN.getId());
            inventoryIn.setRowSts(RowSts.EFFECTIVE.getId());
            inventoryIn.setSourceBillType(BillType.CHANGEORDER.getId());
            inventoryIn.setCode(billCodeBuilderService.getBillCode(BillType.STOREIN.getId()));
            inventoryInService.saveToInventoryIn(inventoryIn);
        }
        return changeOrderdto.getId();
    }

    @Override
    @Transactional
    public String editOrder(ChangeOrderDto changeOrderdto){

        // 组装主表
        super.updateById(changeOrderdto);
        if (changeOrderdto.getDetaillist().size() > 0){
            for (ChangeOrderDtl item: changeOrderdto.getDetaillist()){
                //组装商品详情
                if (item.getId() != null && item.getId().length() > 0)
                    changeOrderDtlService.updateById(item);
                else{
                    item.setSourceId(changeOrderdto.getId());
                    changeOrderDtlService.save(item);
                }
            }
        }

        if (StringUtils.isNotBlank(changeOrderdto.getWarehouseId())) {
            // 配件出库
            inventoryOutService.deleteBySourceId(changeOrderdto.getId());
            InventoryOut inventoryOut = new InventoryOut(changeOrderdto.getId(), changeOrderdto.getCode(), BillType.STOREOUT.getId(), BillType.CHANGEORDER.getId(), changeOrderdto.getWarehouseId(), new Date(), BillStatus.TOSTOCKOUT.getId());
            inventoryOut.setRowSts(RowSts.EFFECTIVE.getId());
            inventoryOutService.saveToInventoryOut(inventoryOut);


            inventoryInService.deleteBySourceId(changeOrderdto.getId());
            InventoryIn inventoryIn = new InventoryIn();
            inventoryIn.setBillStatus(BillStatus.TOSTOCKIN.getId());
            inventoryIn.setWarehouseId(changeOrderdto.getWarehouseId());
            inventoryIn.setPutInTime(new Date());
            inventoryIn.setSourceCode(changeOrderdto.getCode());
            inventoryIn.setSourceId(changeOrderdto.getId());
            inventoryIn.setBillType(BillType.STOREIN.getId());
            inventoryIn.setRowSts(RowSts.EFFECTIVE.getId());
            inventoryIn.setSourceBillType(BillType.CHANGEORDER.getId());
            inventoryIn.setCode(billCodeBuilderService.getBillCode(BillType.STOREIN.getId()));
            inventoryInService.saveToInventoryIn(inventoryIn);
        }
        return changeOrderdto.getId();
    }
}
