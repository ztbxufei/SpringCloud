package com.zhatianbang;

import com.zhatianbang.directexchange.Sender;
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
public class DirectExchangeTest {
    @Autowired
    private Sender sender;

    /*
     * 测试error消息队列
     */
    @Test
    public void testError()throws Exception{
        while(true){
            Thread.sleep(1000);
            this.sender.sendError("Hello RabbitMQ by directexchange this is error");
        }
    }

    /*
    * 测试info消息队列
    */
    @Test
    public void testInfo()throws Exception{
        while(true){
            Thread.sleep(1000);
            this.sender.sendInfo("Hello RabbitMQ by directexchange this is info");
        }
    }
}
