package org.jeecg.modules.saleorder.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.saleorder.dto.SaleOrderDeliveryMtl;
import org.jeecg.modules.saleorder.entity.SaleOrderDeliveryInfo;

import java.util.List;

public interface SaleOrderDeliveryInfoService extends IService<SaleOrderDeliveryInfo> {

    /**
     * 待发货产品列表
     *
     * @param id
     * @param sourceId
     * @return
     */
    public List<SaleOrderDeliveryMtl> getDeliveryMtlList(String id, String sourceId);

    /**
     * 销售出库
     *
     * @param mtls
     * @return
     */
    public Boolean stockOut(List<SaleOrderDeliveryMtl> mtls);

}

