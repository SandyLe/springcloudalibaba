package com.wonders.shgt.dzzz.push.swagger;

import com.wonders.web.swagger.Swagger3Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class PushSwaggerConfig extends Swagger3Config {
    @Bean
    public Docket createRestSysApi(){
        return createRestRouteApi("CERT-LIB-PUSH-API",
                "推送API服务",
                "3.0","/certificate-library/push-api/**",
                "推送API服务:推送接口文档V3.0");
    }
}
