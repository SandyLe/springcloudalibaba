package org.jeecg.modules.saleorder.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.saleorder.entity.SaleOrderDeliveryInfo;

import java.util.List;

public interface SaleOrderDeliveryInfoService extends IService<SaleOrderDeliveryInfo> {

    public String saveSaleOrderDelivery (SaleOrderDeliveryInfo saleOrderDeliveryInfo);
}

