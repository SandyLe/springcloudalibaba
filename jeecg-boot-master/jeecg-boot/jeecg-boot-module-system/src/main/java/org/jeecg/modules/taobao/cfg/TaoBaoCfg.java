package org.jeecg.modules.taobao.cfg;

import org.jeecg.modules.taobao.service.SubscribeService;
import org.jeecg.modules.taobao.service.impl.SubscribeServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TaoBaoCfg {

    @Bean(initMethod="subscribe")
    public SubscribeService subscribeService() {
        return new SubscribeServiceImpl();
    }
}
