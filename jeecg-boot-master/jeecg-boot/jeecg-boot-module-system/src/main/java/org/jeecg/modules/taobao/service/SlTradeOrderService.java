package org.jeecg.modules.taobao.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.taobao.entity.SlTradeOrder;

import java.util.List;

public interface SlTradeOrderService extends IService<SlTradeOrder> {
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
    /**
     * 根据交易ID查询
     * @param tid
     */
    public List<SlTradeOrder> findByTid(Long tid);
}
