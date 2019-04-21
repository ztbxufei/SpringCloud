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
     * 测试结果
     * key: ApplyInfo::UserApplyService.getApplyInfo[38-201904-1555320189295]
     * TTL: 1000
     */
    @Test
    public void getApplyInfoTest(){
        Map<String, Object> applyInfo = userApplyService.getApplyInfo("38-201904-1555320189295");
        System.out.println(applyInfo);
    }

    /**
     * 测试结果
     * key: ApplyInfo2::UserApplyService.getApplyInfo2[38-201904-1555320189295]
     * TTL: 600
     */
    @Test
    public void getApplyInfo2Test(){
        Map<String, Object> applyInfo = userApplyService.getApplyInfo2("38-201904-1555320189295");
        System.out.println(applyInfo);
    }

    /**
     * 测试结果
     * key: userInfoCache::38-201904-1555320189295
     * TTL: 600
     */
    @Test
    public void getApplyInfo3Test(){
        Map<String, Object> applyInfo = userApplyService.getApplyInfo3("38-201904-1555320189295");
        System.out.println(applyInfo);
    }

    /**
     * 测试结果
     * key: catchMap::songwei
     * TTL: 600
     */
    @Test
    public void findByIdTest(){
        Map<String, Object> applyInfo = userApplyService.findById("songwei");
        System.out.println(applyInfo);
    }


    /**
     * 测试结果
     * key: AllInfo::UserApplyService.getAllInfo[songwei6-201809-1537422959594]
     * TTL: 18000
     */
    @Test
    public void getAllInfoTest(){
        userApplyService.getAllInfo("songwei","6-201809-1537422959594");
    }
}
