package org.jeecg.modules.saleorder.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.saleorder.entity.SaleOrderReturn;

import java.util.List;

public interface SaleOrderReturnService extends IService<SaleOrderReturn> {

    /**
     * 根据销售订单ID查询
     * @param sourceId
     * @return
     */
    public List<SaleOrderReturn> findBySourceId (String sourceId);
}
