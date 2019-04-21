package com.zhatianbang.controller;

import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by lenovo on 2019/4/21.
 */
@RestController
public class RedisSessionSharedController {

    @RequestMapping("setSession")
    public void setSession(HttpServletRequest request){
        request.getSession().setAttribute("userName","songwei");
    }

    @RequestMapping("setSession2")
    public void setSession2(HttpServletRequest request){
        request.getSession().setAttribute("userName","zhuangmeng");
    }

    @RequestMapping("getSession")
    public Object  getSession(HttpServletRequest request){
        Object userName = request.getSession().getAttribute("userName");

        return userName;
    }


}
