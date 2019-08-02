package com.zt.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;

/**
 * 功能描述：websocket连接监听器
 * Created by lenovo on 2019/8/1.
 */
@Component
public class WebSocketConnectListener implements ApplicationListener<SessionConnectEvent> {

    Logger logger = LoggerFactory.getLogger(WebSocketConnectListener.class);

    @Override
    public void onApplicationEvent(SessionConnectEvent sessionConnectEvent) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(sessionConnectEvent.getMessage());
        logger.info("headerAccessor"+headerAccessor);
        logger.info("【ConnectEventListener监听器事件 类型】"+headerAccessor.getCommand().getMessageType());

    }
}
