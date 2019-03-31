package com.zhatianbang.safe;

/**
 * 饿汉式单例
 * 在类加载的时候，就已经进行实例化，无论之后用不用到。
 * 如果该类比较占内存，之后又没用到，就白白浪费了资源。
 * Created by lenovo on 2019/3/31.
 */
public class HungerSingleton {
    private static HungerSingleton ourInstance = new HungerSingleton();

    public static HungerSingleton getInstance() {
        return ourInstance;
    }

    private HungerSingleton() {
    }


    //输出结果：10个线程的获得的实例是一样的，线程安全
    public static void main(String[] args) {
        for (int i = 0; i <10; i++){
            new Thread(()->{
                System.out.println(HungerSingleton.getInstance());
            }).start();
        }
    }

}
