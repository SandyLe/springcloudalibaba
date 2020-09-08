package org.jeecg.modules.taobao.service;

import com.taobao.api.domain.Trade;
import org.jeecg.modules.taobao.dto.MsgTradeDto;
import org.jeecg.modules.taobao.entity.SlTrade;

import java.util.List;

public interface SlTradeService {

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
     * @return
     * @throws Exception
     */
    public Trade queryAndSave (Long tid) throws Exception;
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
}
