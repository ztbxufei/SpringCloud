package com.zhatianbang;

import com.zhatianbang.utils.RedisUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by lenovo on 2019/4/14.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisSetTests {

    @Autowired
    RedisUtil redisUtil;

    /**
     * 将数据放入set缓存
     */
    @Test
    public void sSetTest(){
        redisUtil.sSet("车辆属性","新车","二手车");
        redisUtil.sSet("车辆ID","K5","K3","本田冠道");
        redisUtil.sSet("仓库","华东库","华北库","黑龙江分部库","西南分部库","吉林分部库");
    }

    /**
     * 根据key获取Set中的所有值
     */
    @Test
    public void sGetTest(){
        System.out.println(redisUtil.sGet("车辆属性"));
        System.out.println(redisUtil.sGet("车辆ID"));
        System.out.println(redisUtil.sGet("仓库"));
    }

    /**
     * 将数据放入set缓存(根据已经存在的key 给改key的value新增一个值)
     */
    @Test
    public void sSetTest2(){
        redisUtil.sSet("仓库","新疆库");
    }

    /**
     * 移除值为 value=新疆库 的
     */
    @Test
    public void setRemoveTest(){
        redisUtil.setRemove("仓库","新疆库");
    }

    /**
     * 根据value从一个set中查询,是否存在
     */
    @Test
    public void sHasKeyTest(){
        boolean flag = redisUtil.sHasKey("仓库","华北库");
        System.out.println(flag);
    }
}
