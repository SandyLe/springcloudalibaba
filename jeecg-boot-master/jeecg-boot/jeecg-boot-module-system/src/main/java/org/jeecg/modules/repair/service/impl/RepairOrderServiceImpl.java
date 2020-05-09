package org.jeecg.modules.repair.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.common.enums.BillStatus;
import org.jeecg.common.enums.BillType;
import org.jeecg.modules.basic.service.BillCodeBuilderService;
import org.jeecg.modules.repair.entity.RepairOrder;
import org.jeecg.modules.repair.mapper.RepairOrderMapper;
import org.jeecg.modules.repair.service.RepairOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private BillCodeBuilderService billCodeBuilderService;

    @Override
    @Transactional
    public String saveOrder(RepairOrder repairOrder){
        // 组装主表
        String code = billCodeBuilderService.getBillCode(BillType.REPAIR.getId());
        repairOrder.setBillType(BillType.REPAIR.getId());
        repairOrder.setCode(code);
        repairOrder.setBillStatus(BillStatus.NEW.getId());
        super.save(repairOrder);
        return repairOrder.getId();
    }

    @Override
    @Transactional
    public String editOrder(RepairOrder repairOrder){

        // 组装主表
        super.updateById(repairOrder);
        return repairOrder.getId();
    }
}
