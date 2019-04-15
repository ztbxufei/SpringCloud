package com.zhatianbang;

import com.alibaba.fastjson.JSON;
import com.zhatianbang.utils.RedisUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author: jingfei
 * @Date: 2019/4/15 15:39
 * @Version 1.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisLuaTest {

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private DefaultRedisScript<List<Map>> redisScript;


    /**
     * 此处是调用了lua脚本，根据需要匹配的key查询所有满足条件的hash数据然后转化为map集合
     */
    @Test
    public void testRedisLua() {
        List<String> list = new ArrayList<>();
        list.add("keyTable*");
        // 同一封装成List<String> 如有需要可按照步骤自己手动添加其他格式
        List<String> strList = redisUtil.execute(redisScript,list);
        List<Map> mapList = strList.stream().map(str -> JSON.parseObject(str)).collect(Collectors.toList());
        mapList.stream().forEach(System.out::println);
        System.out.println(mapList);
    }
}
