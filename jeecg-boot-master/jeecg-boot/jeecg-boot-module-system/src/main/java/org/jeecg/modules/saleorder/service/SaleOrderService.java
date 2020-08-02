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

    /**
     * 根据编号获取
     * @param code
     * @param companyId
     * @return
     */
    public SaleOrder getOneByCode (String code, String companyId);
}
