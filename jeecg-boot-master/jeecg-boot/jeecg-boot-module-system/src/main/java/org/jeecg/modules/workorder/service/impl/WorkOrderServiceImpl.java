package org.jeecg.modules.workorder.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
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
        String code = workOrderdto.getCode();
        if (StringUtils.isBlank(code)) {
            code = billCodeBuilderService.getBillCode(BillType.WORKORDER.getId());
            workOrderdto.setCode(code);
        }
        if (null == workOrderdto.getBillStatus() || BillStatus.NEW.getId() == workOrderdto.getBillStatus()) {
            if (StringUtils.isNotBlank(workOrderdto.getOperateUserId())) {
                workOrderdto.setBillStatus(BillStatus.ARRANGED.getId());
            } else {
                workOrderdto.setBillStatus(BillStatus.NEW.getId());
            }
        }
        workOrderdto.setRowSts(RowSts.EFFECTIVE.getId());
        super.saveOrUpdate(workOrderdto);

        inventoryOutService.deleteBySourceId(workOrderdto.getBillType(), workOrderdto.getId());

        //工单单子表
        if (CollectionUtils.isNotEmpty(workOrderdto.getDetaillist())){
            List<WorkOrderDtl> mtls = workOrderdto.getDetaillist().stream().filter(o-> StringUtils.isNotBlank(o.getMtlId())).collect(Collectors.toList());
            mtls.stream().forEach(o->{
                //工单商品详情
                o.setCode(workOrderdto.getCode());
                o.setSourceId(workOrderdto.getId());
                workOrderDtlService.saveOrUpdate(o);
            });

            // 配件出库
            InventoryOut inventoryOut = new InventoryOut(workOrderdto.getId(), workOrderdto.getCode(), BillType.INVENTORYOUT.getId(), BillType.WORKORDER.getId(), BillStatus.TOSTOCKOUT.getId());
            inventoryOut.setRowSts(RowSts.EFFECTIVE.getId());
            inventoryOut.setCompanyId(workOrderdto.getCompanyId());
            inventoryOutService.saveToInventoryOut(inventoryOut);
        }

        return workOrderdto.getId();
    }

    @Override
    public List<WorkOrder> findBySourceId(String sourceId, Integer workType) {

        LambdaQueryWrapper<WorkOrder> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(WorkOrder::getSourceId, sourceId);
        queryWrapper.eq(WorkOrder::getWorkTypeId, workType);
        return super.list(queryWrapper);
    }
}
