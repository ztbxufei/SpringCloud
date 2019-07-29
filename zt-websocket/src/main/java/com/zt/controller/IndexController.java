package com.zt.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by lenovo on 2019/7/29.
 */
@Controller
public class IndexController {

    @RequestMapping("/")
    @ResponseBody
    public String IndexController(){
        return "It is ok ！";
    }
}
