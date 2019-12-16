package com.zhatianbang.utils;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * 描述：
 * Created by lenovo on 2019/8/28.
 */
public class UrlUtil {

    /**
     * 功能描述:
     * <将中文路径进行URLEncoder.encode编码转换>
     * @param args 1
     * @return : void
     * @author : zm
     * @date : 2019/8/28
     */
    public static void main(String[] args) throws IOException {
        String s="http://127.0.0.1:8090/AD/c30/文档/PDF/测试.pdf";
        StringBuffer sb = new StringBuffer();
        for(int i=0;i<s.length();i++){
            char a=s.charAt(i);
            //将中文UTF-8编码
            if(a>127){
                sb.append(URLEncoder.encode(String.valueOf(a), "utf-8"));
            }else{
                sb.append(String.valueOf(a));
            }
        }
        System.out.println(sb.toString());
        //编码后URL访问测试:apache的 pdfbox-1.8
        String path = sb.toString();
        URL url = new URL(path);
        HttpURLConnection httpUrl =(HttpURLConnection) url.openConnection();

    }
}
