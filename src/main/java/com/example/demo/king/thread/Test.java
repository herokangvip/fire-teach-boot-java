package com.example.demo.king.thread;

public class Test {
    public static void main(String[] args) throws Exception {
        Thread currentThread = Thread.currentThread();
        MyThread myThread = new MyThread();
        myThread.start();
        //Thread.sleep(20L);
        System.out.println("main方法中测试线程是否被中断标记"+myThread.isInterrupted());
        myThread.interrupt();
        Thread.sleep(500L);
        System.out.println("main方法中测试线程是否被中断标记"+myThread.isInterrupted());
        /*currentThread.interrupt();
        try {
            System.out.println("main:"+currentThread.isInterrupted());
            System.out.println("main:"+currentThread.isInterrupted());
            System.out.println("main:"+currentThread.isInterrupted());
            Thread.sleep(900L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/

    }
}
