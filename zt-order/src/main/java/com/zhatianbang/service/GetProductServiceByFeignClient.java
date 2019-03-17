package com.zhatianbang.service;

import com.zhatianbang.fallback.ProductClientFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by lenovo on 2019/3/10.
 */
@FeignClient(name = "product-service",fallback = ProductClientFallback.class)
@Component
public interface GetProductServiceByFeignClient {

    @GetMapping("/product/service/getProductList")
    String getProductList();

    @GetMapping("/product/service/getProductInfo")
    String getProductInfoById(@RequestParam("id") int id);
}
