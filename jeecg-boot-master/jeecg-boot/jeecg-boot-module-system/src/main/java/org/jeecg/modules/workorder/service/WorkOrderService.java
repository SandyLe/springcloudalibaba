package org.jeecg.modules.workorder.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.workorder.dto.WorkOrderDto;
import org.jeecg.modules.workorder.entity.WorkOrder;

/**
 * @Description:
 * @Author: lixt
 * @Date: 2020/04/04 16:13
 * @Version: V1.0
 */
public interface WorkOrderService extends IService<WorkOrder> {

    public String saveOrder(WorkOrderDto assembledto);
    public String editOrder(WorkOrderDto assembledto);
}
