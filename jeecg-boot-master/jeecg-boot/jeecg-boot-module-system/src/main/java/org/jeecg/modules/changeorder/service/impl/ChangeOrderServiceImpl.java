package org.jeecg.modules.changeorder.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.jeecg.common.enums.BillStatus;
import org.jeecg.common.enums.BillType;
import org.jeecg.common.enums.RowSts;
import org.jeecg.modules.basic.service.BillCodeBuilderService;
import org.jeecg.modules.inventory.entity.InventoryOut;
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
    @Autowired
    private BillCodeBuilderService billCodeBuilderService;

    @Override
    @Transactional
    public String saveOrder(ChangeOrderDto changeOrderdto){
        // 组装主表
        String code = billCodeBuilderService.getBillCode(BillType.WORKORDER.getId());
        changeOrderdto.setCode(code);
        changeOrderdto.setBillStatus(BillStatus.NEW.getId());
        super.save(changeOrderdto);

        //组装单子表
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
            InventoryOut inventoryOut = new InventoryOut(changeOrderdto.getId(), changeOrderdto.getCode(), BillType.STOREOUT.getId(), BillType.WORKORDER.getId(), changeOrderdto.getWarehouseId(), new Date(), BillStatus.TOSTOCKOUT.getId());
            inventoryOut.setRowSts(RowSts.EFFECTIVE.getId());
            inventoryOutService.saveToInventoryOut(inventoryOut);
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

        inventoryOutService.deleteBySourceId(changeOrderdto.getId());
        if (StringUtils.isNotBlank(changeOrderdto.getWarehouseId())) {
            // 配件出库
            InventoryOut inventoryOut = new InventoryOut(changeOrderdto.getId(), changeOrderdto.getCode(), BillType.STOREOUT.getId(), BillType.WORKORDER.getId(), changeOrderdto.getWarehouseId(), new Date(), BillStatus.TOSTOCKOUT.getId());
            inventoryOut.setRowSts(RowSts.EFFECTIVE.getId());
            inventoryOutService.saveToInventoryOut(inventoryOut);
        }
        return changeOrderdto.getId();
    }
}
