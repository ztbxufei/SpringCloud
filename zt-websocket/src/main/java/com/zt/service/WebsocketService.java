package com.zt.service;

import org.springframework.stereotype.Service;

/**
 * Created by lenovo on 2019/7/29.
 */
@Service
public interface WebsocketService {

    void sendMessageToCbhbankChart(String json,String user);

    void sendMessageToAllUser(String json);
}
