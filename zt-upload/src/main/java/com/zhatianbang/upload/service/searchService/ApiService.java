package com.zhatianbang.upload.service.searchService;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @Author: jingfei
 * @Date: 2019/3/11 19:38
 * @Version 1.0
 */
@FeignClient(value = "search-service", fallback = ApiServiceError.class)
public interface ApiService {
    // 定义要请求的接口的路径
    @RequestMapping(value = "/port", method = RequestMethod.GET)
    String index();
}
