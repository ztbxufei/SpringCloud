package com.zhatianbang.controller;

import com.zhatianbang.domain.JsonData;
import com.zhatianbang.jms.MsgProducer;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;

/**
 * Created by lenovo on 2019/4/6.
 */
@RestController
@RequestMapping("/order/mq")
public class UseRocketmqOrderController {

    //注入rocketmq生产者组件
    @Autowired
    private MsgProducer msgProducer;


    @GetMapping("productOrderMsg")
    public Object order(String msg,String tag) throws UnsupportedEncodingException, InterruptedException, RemotingException, MQClientException, MQBrokerException {
        /**
         * 创建一个消息实例，包含topic,tag 和 消息体
         */
        Message message = new Message("testTopic",tag,msg.getBytes(RemotingHelper.DEFAULT_CHARSET));
        SendResult result = msgProducer.getProducer().send(message);
        System.out.println("发送响应：MsgId:" +result.getMsgId()+"，发送状态:"+result.getSendStatus());
        return JsonData.buildSuccess();
    }
}
