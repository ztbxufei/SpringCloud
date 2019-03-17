package com.zhatianbang.domain;

import java.io.Serializable;

/**
 * Created by lenovo on 2019/3/6.
 */
public class ProductInfo implements Serializable {

    private String product_name;      // 商品名称
    private String produat_price;     // 商品价格
    private String product_picture;   // 商品图片
    private String product_color;     // 商品颜色
    private String product_number;    // 商品数量


    public ProductInfo(String product_name, String produat_price, String product_picture, String product_color, String product_number) {
        this.product_name = product_name;
        this.produat_price = produat_price;
        this.product_picture = product_picture;
        this.product_color = product_color;
        this.product_number = product_number;
    }

    public ProductInfo() {
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getProduat_price() {
        return produat_price;
    }

    public void setProduat_price(String produat_price) {
        this.produat_price = produat_price;
    }

    public String getProduct_picture() {
        return product_picture;
    }

    public void setProduct_picture(String product_picture) {
        this.product_picture = product_picture;
    }

    public String getProduct_color() {
        return product_color;
    }

    public void setProduct_color(String product_color) {
        this.product_color = product_color;
    }

    public String getProduct_number() {
        return product_number;
    }

    public void setProduct_number(String product_number) {
        this.product_number = product_number;
    }
}

