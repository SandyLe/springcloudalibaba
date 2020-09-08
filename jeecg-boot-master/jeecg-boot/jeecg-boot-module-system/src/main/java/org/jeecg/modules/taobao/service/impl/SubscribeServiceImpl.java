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
                try {
                    if (TaobaoMessageConstant.TAOBAO_TRADE_TRADECREATE.equals(message.getTopic())) {

                        MsgTradeDto dto = JSONObject.parseObject(message.getContent(), MsgTradeDto.class);
                        tradeService.syncTrade(dto);

                    }




                    System.out.println("==============================taobao.message.start=====================================");
                    System.out.println(message.getContent());
                    System.out.println(message.getTopic());
                    System.out.println("==============================taobao.message.end=====================================");
                } catch (Exception e) {
                    e.printStackTrace();
                    status.fail(); // 消息处理失败回滚，服务端需要重发
                    // 重试注意：不是所有的异常都需要系统重试。
                    // 对于字段不全、主键冲突问题，导致写DB异常，不可重试，否则消息会一直重发
                    // 对于，由于网络问题，权限问题导致的失败，可重试。
                    // 重试时间 5分钟不等，不要滥用，否则会引起雪崩
                }
            }
        });
        client.connect("ws://mc.api.taobao.com"); // 消息环境地址：ws://mc.api.tbsandbox.com/
        System.out.println("---------------订阅淘宝成功！----------");
    }
}
