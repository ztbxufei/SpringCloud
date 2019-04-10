package com.zhatianbang;

import com.zhatianbang.topicexchange.OrderSender;
import com.zhatianbang.topicexchange.ProductSender;
import com.zhatianbang.topicexchange.UserSender;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by lenovo on 2019/4/10.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class TopicExchangeTest {
    @Autowired
    private UserSender usersender;
    @Autowired
    private ProductSender productsender;
    @Autowired
    private OrderSender ordersender;
    /*
    * 测试消息队列
    */
    @Test
    public void test1(){
        this.usersender.UserSend("UserSender.....");
        this.productsender.ProductSend("ProductSender....");
        this.ordersender.OrderSend("OrderSender......");
    }
}
