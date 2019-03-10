package com.zhatianbang.search.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: jingfei
 * @Date: 2019/3/6 17:25
 * @Version 1.0
 */
@RestController
public class HelloController {

    @Value("${server.port}")
    private String port;

    @RequestMapping("/")
    public String index(){
        return "Hello World" + port;
    }

    @RequestMapping("/port")
    public String port(){
        return "Hello World" + port;
    }
}
