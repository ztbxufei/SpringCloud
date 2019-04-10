package com.zhatianbang;

import com.zhatianbang.fanoutexchange.FanoutSender;
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
public class FanoutExchangeTest {

    @Autowired
    FanoutSender fanoutSender;

    @Test
    public void  fanoutTest() throws Exception{
        while (true){
              Thread.sleep(1000L);
            fanoutSender.FanoutSender("push------");
        }
    }

}
