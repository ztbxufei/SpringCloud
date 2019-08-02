package com.zt.controller;

import com.alibaba.fastjson.JSONObject;
import com.zt.service.impl.WebsocketServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lenovo on 2019/7/29.
 */
@Component
public class Timingtask {

    @Autowired
    WebsocketServiceImpl websocketService;
    /**
     * 定时任务1分钟执行一次 每小时整开始执行,模拟后台推送数据
     */
    //@Scheduled(cron = "0 0/1 * * * ?")
    public void loanResultQueryCronOneHour() throws InterruptedException {
        Map<String,Object> map  = new HashMap<String,Object>();
        map.put("time",new Date());
        map.put("name","cbhbank_chart1");
        map.put("product_name","万方众信");
        map.put("count","180000");
        websocketService.sendMessageToCbhbankChart(JSONObject.toJSONString(map),"cbhbank_chart1");

        Thread.sleep(1000L);
        map.put("time",new Date());
        map.put("name","cbhbank_chart2");
        map.put("product_name","万方众信");
        map.put("count","180000");
        websocketService.sendMessageToCbhbankChart(JSONObject.toJSONString(map),"cbhbank_chart2");

        Thread.sleep(1000L);
        websocketService.sendMessageToAllUser("公告：今晚8点系统更新及时安排好手头工作！");
    }
}
