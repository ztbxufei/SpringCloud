package com.zt.service.impl;

import com.zt.model.InMessage;
import com.zt.model.OutMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

/**
 * Created by lenovo on 2019/7/31.
 */
@Service
public class SockJSWebSocketService {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;


    /**
     * 点对点推送（简单版单人聊天）
     * @param message
     */
    public void sendChatMessage(InMessage message){
        System.out.println("/chat/single"+message.getTo());
        simpMessagingTemplate.convertAndSend("/chat/single/"+message.getTo(),
                new OutMessage(message.getFrom()+" 发送:"+ message.getContent()));
    }

    /**
     * 向前端推送JVM相关信息
     */
    public void sendServerInfo() {

        int processors = Runtime.getRuntime().availableProcessors();

        Long freeMem = Runtime.getRuntime().freeMemory();

        Long maxMem = Runtime.getRuntime().maxMemory();

        String message = String.format("服务器可用处理器:%s; 虚拟机空闲内容大小: %s; 最大内存大小: %s", processors,freeMem,maxMem );

        simpMessagingTemplate.convertAndSend("/topic/server_info",new OutMessage(message));

    }



}
