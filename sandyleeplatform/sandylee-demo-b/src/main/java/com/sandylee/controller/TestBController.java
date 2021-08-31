package com.sandylee.controller;

import com.sandylee.api.DemoAService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * @author
 * @desc
 * @date 2021/8/28 2:25 PM
 */
@Slf4j
@RestController
//@RefreshScope
public class TestBController {
/*
    @Value("${user.name}")
    private String userName;*/
    @Autowired
    LoadBalancerClient loadBalancerClient;
    @Autowired
    DemoAService demoAService;

    @GetMapping("/helloB")
    public String hello(@RequestParam String name) {

//        log.info("invoked name: " + name);
        /*ServiceInstance serviceInstance = loadBalancerClient.choose("demoA");
        String url = serviceInstance.getUri() + "/hello?name=" + "didi";
        RestTemplate restTemplate = new RestTemplate();
        String result = restTemplate.getForObject(url, String.class);
        return "Invoke : " + url + ", return : " + result;*/
        return  demoAService.hello("Yawen");
    }
}
