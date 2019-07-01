package com.zhatianbang.controller;

import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Created by lenovo on 2019/4/21.
 */
@RestController
public class RedisSessionSharedController {

    @RequestMapping("setSession")
    public void setSession(HttpServletRequest request){
        HttpSession session = request.getSession();

        System.out.println("当前会话sessionid:"+session.getId());
        session.setAttribute("userName","张三");
    }

    @RequestMapping("setSession2")
    public void setSession2(HttpServletRequest request){
        HttpSession session = request.getSession();
        System.out.println("当前会话sessionid:"+session.getId());
        session.setAttribute("userName","李四");
    }

    @RequestMapping("getSession1")
    public Object  getSession(HttpServletRequest request){
        HttpSession session = request.getSession();
        System.out.println("当前会话sessionid"+session.getId());
        Object userName = request.getSession().getAttribute("userName");
        return userName;
    }
    @RequestMapping("getSession2")
    public Object  getSession2(HttpServletRequest request){
        HttpSession session = request.getSession();
        System.out.println("当前会话sessionid"+session.getId());
        Object userName = request.getSession().getAttribute("userName");
        return userName;
    }

    @RequestMapping("deleteSession1")
    public void deleteSession1(HttpServletRequest request){
        HttpSession session = request.getSession();
        System.out.println("当前会话sessionid"+session.getId());
        session.removeAttribute("userName");
    }

    @RequestMapping("deleteSession2")
    public void deleteSession2(HttpServletRequest request){
        HttpSession session = request.getSession();
        System.out.println("当前会话sessionid"+session.getId());
        session.removeAttribute("userName");
    }



}
