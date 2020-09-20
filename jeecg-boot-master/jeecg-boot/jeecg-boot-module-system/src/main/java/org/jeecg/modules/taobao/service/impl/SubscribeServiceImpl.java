package org.jeecg.modules.taobao.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.taobao.api.internal.tmc.Message;
import com.taobao.api.internal.tmc.MessageHandler;
import com.taobao.api.internal.tmc.MessageStatus;
import com.taobao.api.internal.tmc.TmcClient;
import org.jeecg.common.constant.TaobaoMessageConstant;
import org.jeecg.modules.taobao.dto.MsgTradeDto;
import org.jeecg.modules.taobao.service.SubscribeService;
import org.jeecg.modules.taobao.service.SlTradeService;
import org.springframework.beans.factory.annotation.Autowired;

public class SubscribeServiceImpl implements SubscribeService {

    @Autowired
    private SlTradeService tradeService;
    @Override
    public void subscribe() throws Exception {
        TmcClient client = new TmcClient("31020098", "b05879570f97a1fbf84d35293c12500b", "default"); // 关于default参考消息分组说明
        client.setMessageHandler(new MessageHandler() {
            public void onMessage(Message message, MessageStatus status) {
                System.out.println("==============================taobao.message.start=====================================");
                System.out.println("message topic:  " + message.getTopic());
                System.out.println(JSONObject.toJSON(message));
                try {
                    if (TaobaoMessageConstant.TAOBAO_TRADE_TRADECREATE.equals(message.getTopic())) {
                        // 订单创建
                        MsgTradeDto dto = JSONObject.parseObject(message.getContent(), MsgTradeDto.class);
                        tradeService.queryFromTaobaoAndSave (dto.getTid(), message.getTopic());
                    }
                    if (TaobaoMessageConstant.TAOBAO_TRADE_TRADEBUYERPAY.equals(message.getTopic())) {
                        // 买家付完款，或万人团买家付完尾款
                        MsgTradeDto dto = JSONObject.parseObject(message.getContent(), MsgTradeDto.class);
                        tradeService.updateTradeStatus(dto.getTid(), dto.getStatus(), message.getTopic());
                    }
                    if (TaobaoMessageConstant.TAOBAO_TRADE_TRADECHANGED.equals(message.getTopic())) {
                        // 订单信息变更消息
                        MsgTradeDto dto = JSONObject.parseObject(message.getContent(), MsgTradeDto.class);
                        tradeService.queryFromTaobaoAndSave (dto.getTid(), message.getTopic());

                    }
                    if (TaobaoMessageConstant.TAOBAO_TRADE_TRADESUCCESS.equals(message.getTopic())) {
                        // 交易成功消息
                        MsgTradeDto dto = JSONObject.parseObject(message.getContent(), MsgTradeDto.class);
                        tradeService.updateTradeStatus(dto.getTid(), dto.getStatus(), message.getTopic());
                    }
                    if (TaobaoMessageConstant.TAOBAO_LOGISTICS_LOGSTICDETAILTRACE.equals(message.getTopic())) {
                        // 物流详情跟踪消息


                    }
                    if (TaobaoMessageConstant.TAOBAO_TRADE_TRADEDELAYCONFIRMPAY.equals(message.getTopic())) {
                        // 延长收货时间消息


                    }

                    if (TaobaoMessageConstant.TAOBAO_ITEM_ITEMSTOCKCHANGED.equals(message.getTopic())) {
                        // 修改商品库存消息


                    }
                    if (TaobaoMessageConstant.TAOBAO_ITEM_ITEMUPDATE.equals(message.getTopic())) {
                        // 商品更新消息


                    }
                    if (TaobaoMessageConstant.TAOBAO_TRADE_TRADESELLERSHIP.equals(message.getTopic())) {
                        // 卖家发货消息


                    }
                    if (TaobaoMessageConstant.TAOBAO_TRADE_TRADEALIPAYCREATE.equals(message.getTopic())) {
                        // 创建支付宝订单消息
                        MsgTradeDto dto = JSONObject.parseObject(message.getContent(), MsgTradeDto.class);
                        tradeService.updateTradeStatus(dto.getTid(), dto.getStatus(), message.getTopic());
                    }/*
                    if (TaobaoMessageConstant.TAOBAO_TRADE_TRADEDELAYCONFIRMPAY.equals(message.getTopic())) {
                        // 延长收货时间消息


                    }
                    if (TaobaoMessageConstant.TAOBAO_TRADE_TRADEDELAYCONFIRMPAY.equals(message.getTopic())) {
                        // 延长收货时间消息


                    }
                    if (TaobaoMessageConstant.TAOBAO_TRADE_TRADEDELAYCONFIRMPAY.equals(message.getTopic())) {
                        // 延长收货时间消息


                    }
                    if (TaobaoMessageConstant.TAOBAO_TRADE_TRADEDELAYCONFIRMPAY.equals(message.getTopic())) {
                        // 延长收货时间消息


                    }*/

                } catch (Exception e) {
                    e.printStackTrace();
                    status.fail(); // 消息处理失败回滚，服务端需要重发
                    // 重试注意：不是所有的异常都需要系统重试。
                    // 对于字段不全、主键冲突问题，导致写DB异常，不可重试，否则消息会一直重发
                    // 对于，由于网络问题，权限问题导致的失败，可重试。
                    // 重试时间 5分钟不等，不要滥用，否则会引起雪崩
                }
                System.out.println("==============================taobao.message.end=====================================");
            }
        });
        client.connect("ws://mc.api.taobao.com"); // 消息环境地址：ws://mc.api.tbsandbox.com/
        System.out.println("---------------订阅淘宝成功！----------");
    }
}
