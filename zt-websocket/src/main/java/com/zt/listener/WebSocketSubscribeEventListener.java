package com.zt.listener;

import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

/**
 * Created by lenovo on 2019/8/1.
 */
@Component
public class WebSocketSubscribeEventListener implements ApplicationListener<SessionSubscribeEvent> {

    /**
     * 在事件触发的时候调用这个方法
     *
     * StompHeaderAccessor  简单消息传递协议中处理消息头的基类，
     * 通过这个类，可以获取消息类型(例如:发布订阅，建立连接断开连接)，会话id等
     *
     */
    @Override
    public void onApplicationEvent(SessionSubscribeEvent sessionSubscribeEvent) {
        StompHeaderAccessor headerAccessor =  StompHeaderAccessor.wrap(sessionSubscribeEvent.getMessage());
        System.out.println("【SubscribeEventListener监听器事件 类型】"+headerAccessor.getCommand().getMessageType());

    }
}
