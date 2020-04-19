package org.jeecg.modules.repair.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.repair.entity.RepairOrder;

/**
 * @Description:
 * @Author: lixt
 * @Date: 2020/04/04 16:13
 * @Version: V1.0
 */
public interface RepairOrderService extends IService<RepairOrder> {

    public String saveOrder(RepairOrder repairOrder);
    public String editOrder(RepairOrder repairOrder);
}
