package com.wonders.shgt.dzzz.usage.swagger;

import com.wonders.web.swagger.Swagger3Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class UsageSwaggerConfig extends Swagger3Config {
    @Bean
    public Docket createRestSysApi(){
        return createRestRouteApi("CERT-LIB-USAGE-API",
                "用证API服务",
                "3.0","/certificate-library/usage-api/**",
                "用证API服务:用证接口文档V3.0");
    }
}
