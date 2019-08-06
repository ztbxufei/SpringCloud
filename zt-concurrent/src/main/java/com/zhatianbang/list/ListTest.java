package com.zhatianbang.list;

import com.sun.corba.se.spi.ior.ObjectKey;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 描述：
 * Created by lenovo on 2019/8/5.
 */
public class ListTest {
    public static void main(String[] args) {
        List listA = new ArrayList();
        listA.add("1");
        listA.add("2");
        listA.add("3");
        listA.add("4");
        listA.add("5");

        List listB = new ArrayList();
        listB = listA;

        List listC = new ArrayList(listA);

        System.out.println("A:"+listA);
        System.out.println("B:"+listB);
        System.out.println("C:"+listC);
        listA.remove(0);
        System.out.println("A:"+listA);
        System.out.println("B:"+listB);
        System.out.println("C:"+listC);

        Map a = new HashMap();
        a.put("1","a");
        a.put("2","b");
        a.put("3","c");
        a.put("4","d");
        Map b = new HashMap();
        b=a;
        Map c = new HashMap(a);
        System.out.println(a);
        System.out.println(b);
        System.out.println(c);
        a.remove("1");
        System.out.println(a);
        System.out.println(b);
        System.out.println(c);






    }

}
