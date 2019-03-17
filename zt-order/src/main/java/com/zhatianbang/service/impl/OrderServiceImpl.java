package com.zhatianbang.service.impl;

import com.zhatianbang.domain.ProductOrderInfo;
import com.zhatianbang.service.GetProductServiceByFeignClient;
import com.zhatianbang.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by lenovo on 2019/3/7.
 */
@Service
public class OrderServiceImpl implements OrderService {

    //使用ribbon
    @Autowired
    private RestTemplate restTemplate;

    // 使用Feign
    @Autowired
    private GetProductServiceByFeignClient feignClient;


    @Override
    public ProductOrderInfo saveOrder(int userId, String productId) {
        Object obj = restTemplate.getForObject("http://product-service/getProduct?id="+productId, Object.class);

        System.out.println(obj);

        ProductOrderInfo productOrder = new ProductOrderInfo();
        productOrder.setCreateTime(new Date());
        productOrder.setUserId(userId);
        productOrder.setTradeNo(UUID.randomUUID().toString());

        return productOrder;
    }

    @Override
    public List getProductList() {
        Object object = restTemplate.getForObject("http://product-service//product/service/getProductList", Object.class);

        return (List) object;
    }

    @Override
    public String getProductListByFeign() {
        String productList = feignClient.getProductList();
        return productList;
    }

    @Override
    public String getProductInfo(int id) {
        String productInfoById = feignClient.getProductInfoById(id);
        return productInfoById;
    }
}
