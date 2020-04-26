package org.jeecg.modules.logistics.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.logistics.dto.LogisticsOrderDto;
import org.jeecg.modules.logistics.entity.LogisticsOrder;

/**
 * @Description:
 * @Author: lixt
 * @Date: 2020/04/04 16:13
 * @Version: V1.0
 */
public interface LogisticsOrderService extends IService<LogisticsOrder> {

    public String saveOrder(LogisticsOrderDto logisticsOrderDto);
    public String editOrder(LogisticsOrderDto logisticsOrderDto);
}
