package com.wonders.shgt.dzzz.signature;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication
public class CertLibSealApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(CertLibSealApiApplication.class, args);
    }

}
