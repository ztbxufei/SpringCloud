package com.zhatianbang.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

/**
 * Created by lenovo on 2019/3/10.
 */
public class JsonUtil {
    private static final ObjectMapper objectMappper = new ObjectMapper();


    /**
     * json字符串转JsonNode对象的方法
     */
    public static JsonNode str2JsonNode(String str){
        try {
            return  objectMappper.readTree(str);
        } catch (IOException e) {
            return null;
        }
    }
}
