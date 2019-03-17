package com.zhatianbang.controller;

import com.zhatianbang.domain.ProductInfo;
import com.zhatianbang.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by lenovo on 2019/3/6.
 */
@RestController
@RequestMapping("/product/service")
public class ProductController {

    @Autowired
    ProductService productService;

    @Value("${server.port}")
    private String port;


    /**
     * 获取商品列表
     * @return
     */
    @RequestMapping("/getProductList")
    public List getProductList(){

        return productService.getProductList();
    }

    /**
     * 获取商品详情
     *
     * @param i
     * @return
     */
    @RequestMapping("/getProduct")
    public ProductInfo getProduct(@RequestParam("id") Integer i){
        ProductInfo productInfo = productService.getProductPrice(i);
        productInfo.setProduct_name(productInfo.getProduct_name()+"   data from port："+port);
        return productInfo;
    }

    /**
     * 获取商品详情
     *
     * @param i
     * @return
     */
    @RequestMapping("/getProductInfo")
    public ProductInfo getProductInfo(@RequestParam("id") Integer i){
        ProductInfo productInfo = productService.getProductInfo(i);
        productInfo.setProduct_name(productInfo.getProduct_name()+"   data from port："+port);
        return productInfo;
    }
}
