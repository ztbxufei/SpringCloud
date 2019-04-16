package com.zhatianbang;

import com.zhatianbang.mapper.UserMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by lenovo on 2019/4/15.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisCatchTest {
    @Autowired
    UserMapper userMapper;
    /**
     * 测试 根据key取出hash值
     */
    @Test
    public void hMGetTest(){
        System.out.println(userMapper.getUserByUsername("songwei"));
    }
}
