package com.zhatianbang.service;


import com.zhatianbang.domain.ProductInfo;

import java.util.List;

/**
 * Created by lenovo on 2019/3/6.
 */
public interface ProductService {
    /**
     * 获取订单列表
     * @return
     */
    List<ProductInfo> getProductList();

    /**
     * 获取订单详情
     * @param i
     * @return
     */
    ProductInfo getProductPrice(Integer i);

    ProductInfo getProductInfo(int id);
}
