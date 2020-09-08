package org.jeecg.modules.taobao.service;

import org.jeecg.modules.taobao.entity.SlTradeOrder;

import java.util.List;

public interface SlTradeOrderService {
    /**
     * 保存
     * @param slTradeOrder
     */
    public boolean saveSlTradeOrder(SlTradeOrder slTradeOrder);
    /**
     * 批量保存
     * @param slTradeOrderList
     */
    public boolean saveSlTradeOrderBatch(List<SlTradeOrder> slTradeOrderList);
}
