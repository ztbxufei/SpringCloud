package com.zhatianbang;

import com.zhatianbang.utils.RedisUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 2019/4/14.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisListTests {

    @Autowired
    RedisUtil redisUtil;

    /**
     * 将list放入缓存
     *
     */
    @Test
    public void lSetTest(){
        List<String> list = new ArrayList<>();
        list.add("东风悦达起亚");
        list.add("K5");
        list.add("2015款");
        list.add("黑色");
        list.add("7档手自一体");
        list.add("合资");
        redisUtil.lSet("ESDESWADDF",list);

    }

    /**
     * 获取list缓存内容
     *
     */
    @Test
    public void lGetTest(){
        System.out.println(redisUtil.lGet("ESDESWADDF",0,-1));

    }
}
