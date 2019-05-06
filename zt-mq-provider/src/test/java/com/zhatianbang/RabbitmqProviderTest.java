package com.zhatianbang;

import com.alibaba.fastjson.JSON;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import java.util.HashMap;
import java.util.Map;

/**
 *功能描述：测试rabbitmq发送消息
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class RabbitmqProviderTest {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Test
    public void testJsonRabbitMq(){
        MessageProperties messageProperties = new MessageProperties();
        //设置消息格式为application/json，其他采用默认值
        messageProperties.setContentType("application/json");
        // 使用map消息发送模板处理
        Map paramMap = new HashMap();
        paramMap.put("carFaeId","LFMK440F4J3378876");
        paramMap.put("carIdCodeType","车辆唯一标示类型");
        paramMap.put("carIdCode","车辆唯一标示");
        paramMap.put("carName","车辆名称");
        paramMap.put("carSeries","车系");
        paramMap.put("vehicleModel","车型");
        paramMap.put("carBrank","品牌");
        paramMap.put("carLevel","级别");
        paramMap.put("tsnDsc","变速箱描述");
        paramMap.put("porType","排量");
        paramMap.put("carQly","汽车动力类型");
        paramMap.put("carCts","整车质保");
        paramMap.put("carMktTme","年份款");
        paramMap.put("carJitVte","国产合资进口");
        paramMap.put("carClr","颜色");
        paramMap.put("status","待售");
        Message message = new Message(JSON.toJSONString(paramMap).getBytes(), messageProperties);
        rabbitTemplate.convertAndSend("exchange.cjs.zxph","routeKey.cjs.zxph", message);
    }
}
