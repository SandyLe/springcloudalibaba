package com.sandylee;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author
 * @desc
 * @date 2021/8/27 8:55 AM
 */
@EnableDiscoveryClient
@SpringBootApplication
@ComponentScan(basePackages = {"com.sandylee.controller", "com.sandylee.api"})
public class DemoAApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoAApplication.class, args);
    }
}
