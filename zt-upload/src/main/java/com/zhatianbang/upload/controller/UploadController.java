package com.zhatianbang.upload.controller;

import com.zhatianbang.upload.service.searchService.ApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: jingfei
 * @Date: 2019/03/10 14:06
 * @Description:
 */
@RestController
public class UploadController {

    @Value("${server.port}")
    private String port;

    @Autowired
    private ApiService apiService;

    /**
     * 测试熔断器
     * @return
     */
    @RequestMapping("/index")
    public String index(){
        return apiService.index();
    }
}
