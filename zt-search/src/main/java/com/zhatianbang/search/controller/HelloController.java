package com.zhatianbang.search.controller;

import com.zhatianbang.search.service.HelloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: jingfei
 * @Date: 2019/3/6 17:25
 * @Version 1.0
 */
@RestController
public class HelloController {

    @Autowired
    private HelloService helloService;

    @RequestMapping("/index")
    public String index(){
        int count = helloService.getUserCount();
        return "Hello World" + count;
    }
}
