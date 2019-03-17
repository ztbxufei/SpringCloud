package com.zhatianbang.fallback;

import com.zhatianbang.service.GetProductServiceByFeignClient;
import org.springframework.stereotype.Component;

/**
 * 针对调用的商品服务做降级处理，处理service层
 * Created by zm on 2019/3/11.
 */
@Component
public class ProductClientFallback implements GetProductServiceByFeignClient {
    @Override
    public String getProductList() {
        // ToDo 可根据业务需求做对应的降级处理，这里打印出来做模拟
        System.out.println("feign 调用product-service getProductList方法 异常，请紧急处理");
        return null;
    }

    @Override
    public String getProductInfoById(int id) {
        // ToDo 可根据业务需求做对应的降级处理，这里打印出来做模拟
        System.out.println("feign 调用product-service getProductInfoById方法异常，请紧急处理");
        return null;
    }
}
