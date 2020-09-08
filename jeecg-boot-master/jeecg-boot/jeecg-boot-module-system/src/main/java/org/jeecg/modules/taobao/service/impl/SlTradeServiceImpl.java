package org.jeecg.modules.taobao.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.domain.Order;
import com.taobao.api.domain.Trade;
import com.taobao.api.internal.util.TaobaoUtils;
import com.taobao.api.request.TradeFullinfoGetRequest;
import com.taobao.api.response.TradeFullinfoGetResponse;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.jeecg.modules.taobao.dto.MsgTradeDto;
import org.jeecg.modules.taobao.entity.PrintOrderAssignment;
import org.jeecg.modules.taobao.entity.SlTrade;
import org.jeecg.modules.taobao.entity.SlTradeOrder;
import org.jeecg.modules.taobao.mapper.SlTradeMapper;
import org.jeecg.modules.taobao.service.PrintOrderAssignmentService;
import org.jeecg.modules.taobao.service.SlTradeOrderService;
import org.jeecg.modules.taobao.service.SlTradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SlTradeServiceImpl extends ServiceImpl<SlTradeMapper, SlTrade> implements SlTradeService {

    @Autowired
    private PrintOrderAssignmentService printOrderAssignmentService;
    @Autowired
    private SlTradeOrderService slTradeOrderService;

    @Override
    public void syncTrade(MsgTradeDto msgTradeDto) {
        try {
            TaobaoClient client = new DefaultTaobaoClient("http://gw.api.taobao.com/router/rest", "31020098", "b05879570f97a1fbf84d35293c12500b");
            TradeFullinfoGetRequest req = new TradeFullinfoGetRequest();
            req.setFields("tid,type,status,payment,orders");
            req.setTid(msgTradeDto.getTid());
            TradeFullinfoGetResponse rsp = client.execute(req, "6101107c9971e731a27e8bf4179f36b10341f340637e9c62204151847931");
            System.out.println(rsp.getBody());
            TradeFullinfoGetResponse tradeResp = TaobaoUtils.parseResponse(rsp.getBody(), TradeFullinfoGetResponse.class);
            Trade trade = tradeResp.getTrade();
            queryAndSave (trade.getTid());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Trade queryTradeFromTaoBao (Long tid) throws Exception {

        TaobaoClient client = new DefaultTaobaoClient("http://gw.api.taobao.com/router/rest", "31020098", "b05879570f97a1fbf84d35293c12500b");
        TradeFullinfoGetRequest req = new TradeFullinfoGetRequest();
        req.setFields("seller_nick,pic_path,seller_rate,post_fee,tid,status,payment,orders,receiver_name,receiver_state,receiver_address,receiver_zip,receiver_mobile,receiver_phone,received_payment,receiver_country,receiver_town,shop_pick,num,num_iid,title,type,price,discount_fee,total_fee,created,pay_time,modified,end_time,seller_flag,buyer_nick,has_buyer_message,credit_card_fee,mark_desc,shipping_type,adjust_fee,trade_from,buyer_rate,receiver_city,receiver_district,es_range,es_date,os_date,os_range,cutoff_minutes,delivery_time,collect_time,dispatch_time,sign_time");
        req.setTid(tid);
        TradeFullinfoGetResponse rsp = client.execute(req, "6101107c9971e731a27e8bf4179f36b10341f340637e9c62204151847931");
        System.out.println(rsp.getBody());
        TradeFullinfoGetResponse tradeResp = TaobaoUtils.parseResponse(rsp.getBody(), TradeFullinfoGetResponse.class);
        Trade trade = tradeResp.getTrade();
        return trade;
    }

    @Override
    public Trade queryAndSave (Long tid) throws Exception {

        Trade trade = queryTradeFromTaoBao(tid);

        SlTrade slTrade = new SlTrade();
        BeanUtils.copyProperties(slTrade, trade);
        saveSlTrade(slTrade);

        PrintOrderAssignment printOrderAssignment = new PrintOrderAssignment();
        BeanUtils.copyProperties(printOrderAssignment, trade);
        printOrderAssignmentService.savePrintOrderAssignment(printOrderAssignment);

        List<SlTradeOrder> slTradeOrders = Lists.newArrayList();
        for(Order order : trade.getOrders()){
            SlTradeOrder slTradeOrder = new SlTradeOrder();
            BeanUtils.copyProperties(slTradeOrder, order);
            slTradeOrders.add(slTradeOrder);
        };
        if (CollectionUtils.isNotEmpty(slTradeOrders)) {
            slTradeOrderService.saveSlTradeOrderBatch(slTradeOrders);
        }

        return null;
    }

    @Override
    public boolean saveSlTrade(SlTrade entity) {
        return super.saveOrUpdate(entity);
    }

    @Override
    public boolean saveSlTradeBatch(List<SlTrade> slTradeList) {
        return super.saveOrUpdateBatch(slTradeList);
    }
}
