package com.zhatianbang.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.zhatianbang.domain.ProductOrderInfo;
import com.zhatianbang.service.OrderService;
import com.zhatianbang.utils.JsonUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by lenovo on 2019/3/7.
 */
@RestController
public class OrderController {

    @Autowired
    OrderService orderService;

    @Autowired
    private StringRedisTemplate redisTemplate;


    @RequestMapping("saveOrder")
    public ProductOrderInfo saveOrder(@RequestParam("userId") String userId, @RequestParam("productName") String productName){
        return orderService.saveOrder(Integer.parseInt(userId),productName);
    }

    @RequestMapping("/getProductList")
    public List getProductList(){
        List productList = orderService.getProductList();
        return productList;
    }

    @RequestMapping("/getProductListByFeign")
    public String getProductListByFeign(){
        String productListByFeign = orderService.getProductListByFeign();
        return productListByFeign;
    }

    @RequestMapping("/getProductInfoById")
    @HystrixCommand(fallbackMethod = "saveOrderFail")
    public Object getProductInfoById(@RequestParam("id") String id){
        Map<String,Object> map = new HashMap<String,Object>();
        String productInfo = orderService.getProductInfo(Integer.parseInt(id));
        JsonNode jsonNode = JsonUtil.str2JsonNode(productInfo);
        map.put("code","1");
        map.put("data",jsonNode.get("product_name").toString()+"**********************"+ productInfo);
        return map;
    }

    /**
     * 1.最外层api使用(controller层)，好比异常处理（网络异常，参数或者内部调用问题）
     * 2.api方法上增加 @HystrixCommand(fallbackMethod = "saveOrderFail")
     * 3.编写fallback方法实现，方法签名一定要和api方法签名一致（注意点！！！）
     * 注意，方法签名一定要要和api方法一致，
     * @param id
     * @return
     */
    private Object saveOrderFail(String id){

        // 告警提醒
        String save_order_key = "save-order";
        String save_order_value = redisTemplate.opsForValue().get(save_order_key);

        if(StringUtils.isBlank(save_order_value)){
            // 新启一个线程做异步处理
            new Thread(
                    new Runnable() {
                        @Override
                        public void run() {
                            redisTemplate.opsForValue().set(save_order_key,"save-order-fail",20, TimeUnit.SECONDS);
                            System.out.println("发送紧急短信提醒，服务坏了，发送时间是："+new Date());
                        }
                    }
            ).start();
        }

        Map<String, Object> msg = new HashMap<>();
        msg.put("code", -1);
        msg.put("msg", "抢购人数太多，您被挤出来了，稍等重试");
        return msg;
    }
}
