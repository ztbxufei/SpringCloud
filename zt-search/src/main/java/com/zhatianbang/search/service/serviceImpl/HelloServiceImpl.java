package com.zhatianbang.search.service.serviceImpl;

import com.zhatianbang.search.dao.HelloDao;
import com.zhatianbang.search.service.HelloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: jingfei
 * @Date: 2019/3/6 17:27
 * @Version 1.0
 */
@Service
public class HelloServiceImpl implements HelloService {

    @Autowired
    private HelloDao helloDao;

    @Override
    public int getUserCount() {
        return helloDao.getUserCount();
    }
}
