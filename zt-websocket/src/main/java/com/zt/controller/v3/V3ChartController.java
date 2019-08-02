package com.zt.controller.v3;

import com.zt.model.InMessage;
import com.zt.service.impl.SockJSWebSocketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

/**
 * Created by lenovo on 2019/7/31.
 */
@Controller
public class V3ChartController {

    @Autowired
    SockJSWebSocketService sockJSWebSocketService;

    @MessageMapping("/v3/single/chat")
    public void singleChat(InMessage message){
        System.out.println(message);
        sockJSWebSocketService.sendChatMessage(message);
    }
}
