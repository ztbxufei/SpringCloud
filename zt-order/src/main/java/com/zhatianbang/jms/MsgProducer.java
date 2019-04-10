package com.zhatianbang.jms;

import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * rocketmq生产者
 * Created by lenovo on 2019/4/6.
 */
@Component
public class MsgProducer {

    // 生产者组名
    @Value("${apache.rocketmq.producer.producerGroup}")
    private String producerGroup;

    // NameServer 地址
    @Value("${apache.rocketmq.namesrvAddr}")
    private String nameservAddr;

    private DefaultMQProducer producer;

    public DefaultMQProducer getProducer(){
        return this.producer;
    }

    /**
     * 初始化
     */
    @PostConstruct
    public void init(){
        // 生产者组名
        producer = new DefaultMQProducer(producerGroup);
        // 指定NameServer地址，如有多个地址中间以;隔开
        // 如：producer.setNamesrvAddr("192.168.100.141:9876;192.168.100.142:9876;192.168.100.149:9876");
        producer.setNamesrvAddr(nameservAddr);
        producer.setVipChannelEnabled(false);
        try {
            producer.start();
        } catch (MQClientException e) {
            e.printStackTrace();
        }
        // producer.shutdown();  一般在应用上下文，关闭的时候进行关闭，用上下文监听器
    }
}
