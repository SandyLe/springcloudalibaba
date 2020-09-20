package org.jeecg.modules.taobao.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.taobao.api.domain.Trade;
import org.jeecg.modules.taobao.dto.MsgTradeDto;
import org.jeecg.modules.taobao.entity.SlTrade;

import java.util.List;

public interface SlTradeService extends IService<SlTrade> {

    /**
     * 同步天猫订单
     * @param msgTradeDto
     */
    public void syncTrade(MsgTradeDto msgTradeDto);

    /**
     * 天猫平台查询订单详细信息
     * @param tid
     */
    public Trade queryTradeFromTaoBao (Long tid) throws Exception;

    /**
     * 查询和保存
     * @param tid
     * @param topic
     * @return
     * @throws Exception
     */
    public Trade queryFromTaobaoAndSave (Long tid, String topic) throws Exception;
    /**
     * 保存
     * @param slTrade
     */
    public boolean saveSlTrade(SlTrade slTrade);
    /**
     * 批量保存
     * @param slTradeList
     */
    public boolean saveSlTradeBatch(List<SlTrade> slTradeList);

    /**
     * 更新订单状态
     * @param tid
     * @param status
     * @param topic
     * @return
     */
    public boolean updateTradeStatus (Long tid, String status, String topic) throws Exception ;

    /**
     * 保存
     * @param trade
     * @param topic
     * @return
     * @throws Exception
     */
    public Trade saveOrUpdateTrade (Trade trade, String topic) throws Exception;
}
