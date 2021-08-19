package com.wonders.shgt.dzzz.make.swagger;

import com.wonders.web.swagger.Swagger3Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class PdfSwaggerConfig extends Swagger3Config {
    @Bean
    public Docket createRestSysApi(){
        return createRestRouteApi("CERT-LIB-PDF-API",
                "制证API服务",
                "3.0","/certificate-library/pdf-api/**",
                "制证API服务:PDF接口文档V3.0");
    }
}
