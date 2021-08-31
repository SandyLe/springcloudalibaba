package com.sandylee.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author
 * @desc
 * @date 2021/8/27 9:06 AM
 */
@RestController
@RefreshScope
public class TestController {

    @Value("${user.name}")
    private String userName;

    @GetMapping("/hello")
    public String hello(@RequestParam String name) {

//        log.info("invoked name: " + name);
        return "hello, " + name + ", Configged name is " + userName;
    }
}
