package com.zhatianbang.service;


import com.zhatianbang.domain.ProductOrderInfo;

import java.util.List;

/**
 * Created by lenovo on 2019/3/7.
 */
public interface OrderService {

    ProductOrderInfo saveOrder(int userId, String productId);

    List getProductList();

    String getProductListByFeign();

    String getProductInfo(int id);
}
