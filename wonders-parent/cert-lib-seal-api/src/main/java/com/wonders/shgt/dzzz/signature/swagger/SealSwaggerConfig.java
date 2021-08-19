package com.wonders.shgt.dzzz.signature.swagger;

import com.wonders.web.swagger.Swagger3Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SealSwaggerConfig extends Swagger3Config {
    @Bean
    public Docket createRestSysApi(){
        return createRestRouteApi("CERT-LIB-SEAL-API",
                "签章API服务",
                "3.0","/certificate-library/seal-api/**",
                "签章API服务:签章接口文档V3.0");
    }
}
