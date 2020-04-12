package org.jeecg.modules.repair.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.jeecg.common.enums.BillStatus;
import org.jeecg.common.enums.BillType;
import org.jeecg.common.enums.RowSts;
import org.jeecg.modules.basic.service.BillCodeBuilderService;
import org.jeecg.modules.inventory.entity.InventoryOut;
import org.jeecg.modules.inventory.service.InventoryOutService;
import org.jeecg.modules.repair.dto.RepairOrderDto;
import org.jeecg.modules.repair.entity.RepairOrder;
import org.jeecg.modules.repair.entity.RepairOrderDtl;
import org.jeecg.modules.repair.mapper.RepairOrderMapper;
import org.jeecg.modules.repair.service.RepairOrderDtlService;
import org.jeecg.modules.repair.service.RepairOrderService;
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
public class RepairOrderServiceImpl extends ServiceImpl<RepairOrderMapper, RepairOrder> implements RepairOrderService {

    @Autowired
    private RepairOrderMapper repairOrderMapper;
    @Autowired
    private RepairOrderDtlService repairOrderDtlService;
    @Lazy
    @Autowired
    private InventoryOutService inventoryOutService;
    @Autowired
    private BillCodeBuilderService billCodeBuilderService;

    @Override
    @Transactional
    public String saveOrder(RepairOrderDto repairOrderdto){
        // 组装主表
        String code = billCodeBuilderService.getBillCode(BillType.WORKORDER.getId());
        repairOrderdto.setCode(code);
        repairOrderdto.setBillStatus(BillStatus.NEW.getId());
        super.save(repairOrderdto);

        //组装单子表
        if (CollectionUtils.isNotEmpty(repairOrderdto.getDetaillist())){
            List<RepairOrderDtl> mtls = repairOrderdto.getDetaillist().stream().filter(o-> StringUtils.isNotBlank(o.getMtlId())).collect(Collectors.toList());
            mtls.stream().forEach(o->{
                //组装商品详情
                o.setCode(code);
                o.setSourceId(repairOrderdto.getId());
            });
            repairOrderDtlService.saveBatch(mtls);
        }

        if (StringUtils.isNotBlank(repairOrderdto.getWarehouseId())) {
            // 配件出库
            InventoryOut inventoryOut = new InventoryOut(repairOrderdto.getId(), repairOrderdto.getCode(), BillType.STOREOUT.getId(), BillType.WORKORDER.getId(), repairOrderdto.getWarehouseId(), new Date(), BillStatus.TOSTOCKOUT.getId());
            inventoryOut.setRowSts(RowSts.EFFECTIVE.getId());
            inventoryOutService.saveToInventoryOut(inventoryOut);
        }
        return repairOrderdto.getId();
    }

    @Override
    @Transactional
    public String editOrder(RepairOrderDto repairOrderdto){

        // 组装主表
        super.updateById(repairOrderdto);
        if (repairOrderdto.getDetaillist().size() > 0){
            for (RepairOrderDtl item: repairOrderdto.getDetaillist()){
                //组装商品详情
                if (item.getId() != null && item.getId().length() > 0)
                    repairOrderDtlService.updateById(item);
                else{
                    item.setSourceId(repairOrderdto.getId());
                    repairOrderDtlService.save(item);
                }
            }
        }

        inventoryOutService.deleteBySourceId(repairOrderdto.getId());
        if (StringUtils.isNotBlank(repairOrderdto.getWarehouseId())) {
            // 配件出库
            InventoryOut inventoryOut = new InventoryOut(repairOrderdto.getId(), repairOrderdto.getCode(), BillType.STOREOUT.getId(), BillType.WORKORDER.getId(), repairOrderdto.getWarehouseId(), new Date(), BillStatus.TOSTOCKOUT.getId());
            inventoryOut.setRowSts(RowSts.EFFECTIVE.getId());
            inventoryOutService.saveToInventoryOut(inventoryOut);
        }
        return repairOrderdto.getId();
    }
}
