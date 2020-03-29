package org.jeecg.modules.saleorder.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.basic.dto.DeliveryEditDto;
import org.jeecg.modules.saleorder.entity.SaleOrder;

public interface SaleOrderService extends IService<SaleOrder> {

    /**
     * 发货
     * @param deliveryEditDto
     */
    public void delivery (DeliveryEditDto deliveryEditDto) throws Exception;
}
