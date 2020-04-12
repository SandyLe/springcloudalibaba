package org.jeecg.modules.changeorder.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.changeorder.dto.ChangeOrderDto;
import org.jeecg.modules.changeorder.entity.ChangeOrder;

/**
 * @Description:
 * @Author: lixt
 * @Date: 2020/04/04 16:13
 * @Version: V1.0
 */
public interface ChangeOrderService extends IService<ChangeOrder> {

    public String saveOrder(ChangeOrderDto assembledto);
    public String editOrder(ChangeOrderDto assembledto);
}
