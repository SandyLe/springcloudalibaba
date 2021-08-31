package com.sandylee.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author
 * @desc
 * @date 2021/8/29 4:28 PM
 */
@Component
@FeignClient(name = "demoA")
public interface DemoAService {

    @RequestMapping( value = "/hello",  method = RequestMethod.GET)
    String hello(@RequestParam(value = "name", required = false) String name);
}
