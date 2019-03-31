package com.zhatianbang.lock;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 线程不安全操作代码实例
 * Created by lenovo on 2019/3/31.
 */
public class UnsafeThread {
    private static  int num = 0;
    // 线程计数器 计10个数
    private static CountDownLatch countDownLatch = new CountDownLatch(10);

    private static Lock lock = new ReentrantLock();

    /**
     * 每次调用对num执行++操作
     * 通过手动添加锁和释放锁来达到和synchronized关键字一样的效果
     */
    public static void inCreate(){
        lock.lock();
        num++;
        lock.unlock();
    }

    /**
     * 由于num++操作不是原子性的，当多线程并发调用的时候会造成线程不安全结果
     * @param args
     */
    public static void main(String[] args) {
        for (int i = 0 ; i < 10 ; i++){
            new Thread(()->{
                for (int j = 0 ; j < 100 ; j++){
                    inCreate();
                    try {
                        // 使用线程休眠来模拟该线程执行所需时间
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                //每个线程执行完成之后，调用countDownLatch 线程数减1
                countDownLatch.countDown();
            }).start();
        }
        while(true){
            if(countDownLatch.getCount() == 0){
                System.out.println(num);
                break;
            }

        }
    }
}
