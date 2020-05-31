package org.jeecg.modules.workorder.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.jeecg.common.enums.BillStatus;
import org.jeecg.common.enums.BillType;
import org.jeecg.common.enums.RowSts;
import org.jeecg.modules.basic.service.BillCodeBuilderService;
import org.jeecg.modules.workorder.dto.WorkOrderDto;
import org.jeecg.modules.workorder.entity.WorkOrder;
import org.jeecg.modules.workorder.entity.WorkOrderDtl;
import org.jeecg.modules.workorder.mapper.WorkOrderMapper;
import org.jeecg.modules.workorder.service.WorkOrderDtlService;
import org.jeecg.modules.workorder.service.WorkOrderService;
import org.jeecg.modules.inventory.entity.InventoryIn;
import org.jeecg.modules.inventory.entity.InventoryOut;
import org.jeecg.modules.inventory.service.InventoryInService;
import org.jeecg.modules.inventory.service.InventoryOutService;
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
public class WorkOrderServiceImpl extends ServiceImpl<WorkOrderMapper, WorkOrder> implements WorkOrderService {

    @Autowired
    private WorkOrderMapper workOrderMapper;
    @Autowired
    private WorkOrderDtlService workOrderDtlService;
    @Lazy
    @Autowired
    private InventoryOutService inventoryOutService;
    @Autowired
    private BillCodeBuilderService billCodeBuilderService;

    @Override
    @Transactional
    public String saveOrder(WorkOrderDto workOrderdto){
        // 工单主表
        String code = billCodeBuilderService.getBillCode(BillType.WORKORDER.getId());
        workOrderdto.setCode(code);
        if (null == workOrderdto.getBillStatus()) {
            if (StringUtils.isNotBlank(workOrderdto.getOperateUserId())) {
                workOrderdto.setBillStatus(BillStatus.ARRANGED.getId());
            } else {
                workOrderdto.setBillStatus(BillStatus.NEW.getId());
            }
        }
        workOrderdto.setRowSts(RowSts.EFFECTIVE.getId());
        super.save(workOrderdto);

        //工单单子表
        if (CollectionUtils.isNotEmpty(workOrderdto.getDetaillist())){
            List<WorkOrderDtl> mtls = workOrderdto.getDetaillist().stream().filter(o-> StringUtils.isNotBlank(o.getMtlId())).collect(Collectors.toList());
            mtls.stream().forEach(o->{
                //工单商品详情
                o.setCode(code);
                o.setSourceId(workOrderdto.getId());
            });
            workOrderDtlService.saveBatch(mtls);
        }

        if (StringUtils.isNotBlank(workOrderdto.getWarehouseId())) {
            // 配件出库
            InventoryOut inventoryOut = new InventoryOut(workOrderdto.getId(), workOrderdto.getCode(), BillType.INVENTORYOUT.getId(), BillType.WORKORDER.getId(), workOrderdto.getWarehouseId(), new Date(), BillStatus.TOSTOCKOUT.getId());
            inventoryOut.setRowSts(RowSts.EFFECTIVE.getId());
            inventoryOut.setCompanyId(workOrderdto.getCompanyId());
            inventoryOutService.saveToInventoryOut(inventoryOut);
        }
        return workOrderdto.getId();
    }

    @Override
    @Transactional
    public String editOrder(WorkOrderDto workOrderdto){

        if (null == workOrderdto.getBillStatus() || BillStatus.NEW.getId() == workOrderdto.getBillStatus()) {
            if (StringUtils.isNotBlank(workOrderdto.getOperateUserId())) {
                workOrderdto.setBillStatus(BillStatus.ARRANGED.getId());
            } else {
                workOrderdto.setBillStatus(BillStatus.NEW.getId());
            }
        }
        // 工单主表
        super.updateById(workOrderdto);
        if (workOrderdto.getDetaillist().size() > 0){
            for (WorkOrderDtl item: workOrderdto.getDetaillist()){
                //工单配件详情
                if (item.getId() != null && item.getId().length() > 0)
                    workOrderDtlService.updateById(item);
                else{
                    item.setSourceId(workOrderdto.getId());
                    workOrderDtlService.save(item);
                }
            }
        }

        if (StringUtils.isNotBlank(workOrderdto.getWarehouseId())) {
            inventoryOutService.deleteBySourceId(workOrderdto.getId());
            // 配件出库
            InventoryOut inventoryOut = new InventoryOut(workOrderdto.getId(), workOrderdto.getCode(), BillType.INVENTORYOUT.getId(), BillType.WORKORDER.getId(), workOrderdto.getWarehouseId(), new Date(), BillStatus.TOSTOCKOUT.getId());
            inventoryOut.setRowSts(RowSts.EFFECTIVE.getId());
            inventoryOut.setCompanyId(workOrderdto.getCompanyId());
            inventoryOutService.saveToInventoryOut(inventoryOut);
        }
        return workOrderdto.getId();
    }
}
