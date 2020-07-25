package org.jeecg.modules.saleorder.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.saleorder.entity.SaleOrderAddress;

public interface SaleOrderAddressService extends IService<SaleOrderAddress> {

    public SaleOrderAddress findBySouorceId (String sourceId);

}
