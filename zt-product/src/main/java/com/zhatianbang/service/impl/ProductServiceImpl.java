package com.zhatianbang.service.impl;

import com.zhatianbang.domain.ProductInfo;
import com.zhatianbang.service.ProductService;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by lenovo on 2019/3/6.
 */
@Service
public class ProductServiceImpl implements ProductService {

    private static  final Map<Integer,ProductInfo> productMap = new HashMap<Integer,ProductInfo>();

    static {
        ProductInfo p1 = new ProductInfo("小米9","2999","图1","炫彩黑","300");
        ProductInfo p2 = new ProductInfo("小米8","2299","图2","深空黑","200");
        ProductInfo p3 = new ProductInfo("苹果X","6500","图3","黑色","2000");
        ProductInfo p4 = new ProductInfo("小米笔记本","6599","图4","银色","34");

        productMap.put(1,p1);
        productMap.put(2,p2);
        productMap.put(3,p3);
        productMap.put(4,p4);
    }


    @Override
    public List<ProductInfo> getProductList() {
        Collection<ProductInfo> collection = productMap.values();
        List<ProductInfo> list = new ArrayList<>(collection);
        return list;
    }

    @Override
    public ProductInfo getProductInfo(int id) {

        // 模拟服务调用超时
        /*try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/

        ProductInfo productInfo = productMap.get(id);
        return productInfo;
    }

    @Override
    public ProductInfo getProductPrice(Integer i) {


        return productMap.get(i);
    }
}
