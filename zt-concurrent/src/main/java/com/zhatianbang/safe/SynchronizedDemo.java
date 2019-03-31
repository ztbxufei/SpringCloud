package com.zhatianbang.safe;

/**
 * 深入理解synchronized关键字
   1.修饰普通方法：锁住对象的实例
   2.修饰静态方法：锁住整个类
   3.修饰代码块： 锁住一个对象 synchronized (lock) 即synchronized后面括号里的内容
 * Created by lenovo on 2019/3/31.
 */
public class SynchronizedDemo {


    /**
     * synchronized 修饰普通方法
     */
    public synchronized void  output() throws InterruptedException {
        // 输出当前线程的名字
        System.out.println(Thread.currentThread().getName());
        Thread.sleep(5000L);
    }

    /**
     * synchronized 修饰静态方法
     */
    public static synchronized void  staticOutput() throws InterruptedException {
        // 输出当前线程的名字
        System.out.println(Thread.currentThread().getName());
        Thread.sleep(5000L);
    }

    private  Object ob = new Object();
    /**
     * synchronized 修饰静态方法
     */
    public  void  myOutput() throws InterruptedException {
        synchronized (ob){
            // 输出当前线程的名字
            System.out.println(Thread.currentThread().getName());
            Thread.sleep(5000L);
        }
    }


    /*
    // new对象的两个实例，分别两个线程调用两个实例的synchronized修饰的普通方法,
    // 输出结果：Thread-0 Thread-1 几乎同时输出，说明synchronized修饰普通方法锁住对象的实例
   public static void main(String[] args) {
        SynchronizedDemo synchronizedDemo1 = new SynchronizedDemo();
        SynchronizedDemo synchronizedDemo2 = new SynchronizedDemo();

        new Thread(()->{
            try {
                synchronizedDemo1.output();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        new Thread(()->{
            try {
                synchronizedDemo2.output();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }
*/


   /* // new对象的两个实例，分别两个线程调用两个实例的synchronized修饰的静态方法,
    // 输出结果：Thread-0先输出 线程1执行完之后（休眠结束）Thread-1才输出，说明synchronized修饰静态方法锁住是整个类
    public static void main(String[] args) {
        SynchronizedDemo synchronizedDemo1 = new SynchronizedDemo();
        SynchronizedDemo synchronizedDemo2 = new SynchronizedDemo();

        new Thread(()->{
            try {
                synchronizedDemo1.staticOutput();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        new Thread(()->{
            try {
                synchronizedDemo2.staticOutput();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }*/


    // new对象的1个实例，分别两个线程调用这个实例的synchronized修饰的代码块,
    // 输出结果：Thread-0先输出 线程1执行完之后（休眠结束）Thread-1才输出，
    // 说明synchronized修饰代码块： 锁住一个对象 synchronized (lock) 即synchronized后面括号里的内容
    public static void main(String[] args) {
        SynchronizedDemo synchronizedDemo1 = new SynchronizedDemo();

        new Thread(()->{
            try {
                synchronizedDemo1.myOutput();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        new Thread(()->{
            try {
                synchronizedDemo1.myOutput();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

}
