package com.wonders.shgt.dzzz.usage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication
public class CertLibUsageApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(CertLibUsageApiApplication.class, args);
    }

}
