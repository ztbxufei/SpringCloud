package com.zhatianbang.upload.service.searchService;

import org.springframework.stereotype.Component;

/**
 * @Author: jingfei
 * @Date: 2019/3/11 20:18
 * @Version 1.0
 */
@Component
public class ApiServiceError implements ApiService{
    @Override
    public String index() {
        return "服务器故障";
    }
}
