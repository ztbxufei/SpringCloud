package com.zt.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.zt.server.WebSocketServer;
import com.zt.service.WebsocketService;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lenovo on 2019/7/29.
 */
@Service
public class WebsocketServiceImpl implements WebsocketService {

    /**
     * 给指定用户user发送消息message
     * @param json
     */
    @Override
    public void sendMessageToCbhbankChart(String json,String user) {

        try {
            WebSocketServer.sendToUser(json,user);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 给所有的用户发送消息
     * @param json
     */
    @Override
    public void sendMessageToAllUser(String json) {
        WebSocketServer.sendAll(json);

    }
}
