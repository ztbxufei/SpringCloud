package com.zhatianbang;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by lenovo on 2019/5/20.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class LambdaDemoTest {

    /**
     * 用lambda表达式实现runnable
     */
    @Test
    public void test1(){
        // Java8之前
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("Java8之前新建一个线程");
            }
        }).start();

        // Java8写法
        new Thread(()-> System.out.println("Java8新建一个线程")).start();
    }

    /**
     * 使用Lambda遍历集合
     */
    @Test
    public void test2(){
        // Java 8之前：
        List<String> listDemo = Arrays.asList("Lambdas", "Default Method", "Stream API", "Date and Time API");
       for(String par : listDemo){
           if("Lambdas".equals(par)){
               System.out.println(par);
           }
       }

        // Java 8
        listDemo.forEach(n -> System.out.println(n));
        listDemo.forEach(System.out::println);
    }

    /**
     * 使用lambda表达式过滤字符串
     */
    @Test
    public void test3(){
        Map<String,Object> map1 = new HashMap<String,Object>();
        map1.put("apply_no","11111111");
        map1.put("filename","身份证正面.jpg");
        map1.put("csxbankname","IdCard");
        Map<String,Object> map2 = new HashMap<String,Object>();
        map2.put("apply_no","11111111");
        map2.put("filename","身份证反面.jpg");
        map2.put("csxbankname","IdCard");
        Map<String,Object> map3 = new HashMap<String,Object>();
        map3.put("apply_no","11111111");
        map3.put("filename","手持身份证.jpg");
        map3.put("csxbankname","IdCard");
        Map<String,Object> map4 = new HashMap<String,Object>();
        map4.put("apply_no","11111111");
        map4.put("filename","驾驶证.jpg");
        map4.put("csxbankname","jsz");
        Map<String,Object> map5 = new HashMap<String,Object>();
        map5.put("apply_no","11111111");
        map5.put("filename","车辆照片.jpg");
        map5.put("csxbankname","clzp");

        List<Map> listMap = new ArrayList<>();
        listMap.add(map1);
        listMap.add(map2);
        listMap.add(map3);
        listMap.add(map4);
        listMap.add(map5);

        int index = 1;

        // 原始集合
        listMap.forEach(map-> System.out.println("原始集合："+map));
        System.out.println();

        // 找出filename只包含有“身份证正面.”的集合
        List<Map> filenameMap_zm = listMap.stream().filter(map -> map.get("filename").toString().contains("身份证正面.")).collect(Collectors.toList());
        filenameMap_zm.forEach(map-> System.out.println("只包含有“身份证正面.”的集合"+map));
        System.out.println();

        // 找出filename只包含有“身份证反面.”的集合
        List<Map> filenameMap_fm = listMap.stream().filter(map -> map.get("filename").toString().contains("身份证反面.")).collect(Collectors.toList());
        filenameMap_fm.forEach(map-> System.out.println("只包含有“身份证反面.”的集合"+map));
        System.out.println();

        // 移除filename含有“身份证正面.”元素之后的集合
        if(filenameMap_zm!=null){
            listMap.removeIf(map -> map.get("filename").toString().contains("身份证正面."));
            index++;
            listMap.forEach(map -> System.out.println("移除身份证正面之后的原始集合"+map));
        }
        System.out.println();

        // 移除filename含有“身份证反面.”元素之后的集合
        if(filenameMap_fm!=null){
            listMap.removeIf(map -> map.get("filename").toString().contains("身份证反面."));
            index++;
            listMap.forEach(map -> System.out.println("移除身份证正面和反面之后的原始集合"+map));
        }

    }


    @Test
    public void test4(){
        // 不使用lambda表达式为每个订单加上12%的税
        List<Double> costBeforeTax = Arrays.asList(100.00, 200.00, 300.00, 400.00, 500.00);
        List<Double> oldList = new ArrayList<>();
        for (int i=0 ; i < costBeforeTax.size();i++) {
            oldList.add(i,costBeforeTax.get(i)+costBeforeTax.get(i)*0.12);
        }
        oldList.forEach(cost-> System.out.println(cost));


        List<Double> costBeforeTax2 = Arrays.asList(100.00, 200.00, 300.00, 400.00, 500.00);
        // 使用lambda表达式为每个订单加上12%的税
        List<Double> doubleList = costBeforeTax2.stream().map((cost) -> cost + cost * 0.12).collect(Collectors.toList());
        doubleList.forEach(cost-> System.out.println(cost));

        // 不使用lambda表达式为每个订单加上12%的税求总和
        double total = 0;
        for(double cost:costBeforeTax){
            double price =cost+cost*0.12;
            total = total+price;
        }
        System.out.println("老方法得到的total:"+total);

        // 使用lambda表达式为每个订单加上12%的税求总和
        Double lambdaTotal = costBeforeTax2.stream().map(cost -> cost + cost * 0.12).reduce((a, b) -> a + b).get();
        System.out.println("lambda方法得到的total:"+lambdaTotal);
    }


}
