package com.example.demo.king.thread;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class MyThread extends Thread {
    public static ThreadLocal<Map<String,String>> threadLocal = new ThreadLocal<>();
    private static AtomicInteger num = new AtomicInteger(0);
    @Override
    public void run() {
        int i = 1;
        try {
            for (; i < 10; i++) {
                num.getAndIncrement();
                num.incrementAndGet();
                num.getAndAdd(1);
                num.addAndGet(1);
                num.incrementAndGet();
                if(this.isInterrupted()){
                    System.out.println("====" + i+"==="+Thread.interrupted());
                    throw new InterruptedException();
                }
                boolean interrupted = this.isInterrupted();
                boolean interrupted2 = this.isInterrupted();
                System.out.println("====" + i+"==="+interrupted);
                //Thread.sleep(10L);
                System.out.println("====" + i+"==="+interrupted2);
            }
        } catch (InterruptedException e) {
            String name = this.getName();
            System.out.println(name+"线程被中断了执行到" + i + "次==="+this.isInterrupted());
        }

    }


    public static void main(String[] args) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ArrayList<Object> list = new ArrayList<>();
                    while (true){
                        byte[] arr = new byte[1024*1024];
                        list.add(arr);
                        System.out.println("=======11111111111");
                        Thread.currentThread().sleep(1000L);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (true){
                        System.out.println("222222222222222222");
                        Thread.currentThread().sleep(1000L);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
        thread2.start();
    }
}
