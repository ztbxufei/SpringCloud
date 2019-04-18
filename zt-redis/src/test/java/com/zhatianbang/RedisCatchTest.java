package com.zhatianbang;

import com.zhatianbang.mapper.UserMapper;
import com.zhatianbang.service.impl.UserApplyService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;

/**
 * Created by lenovo on 2019/4/15.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisCatchTest {
    @Autowired
    UserMapper userMapper;
    @Autowired
    UserApplyService userApplyService;
    /**
     * 测试 根据key取出hash值
     */
    @Test
    public void hMGetTest(){
        Map<String, Object> applyInfo = userApplyService.getApplyInfo("6-201809-1537422959594");
        System.out.println(applyInfo);
    }

    /**
     * 测试 根据key取出hash值
     */
    @Test
    public void Test(){
        Map<String, Object> applyInfo = userApplyService.findById("songwei");
        System.out.println(applyInfo);
    }


    @Test
    public void Test2(){
        userApplyService.getAllInfo("songwei","6-201809-1537422959594");
    }
}
