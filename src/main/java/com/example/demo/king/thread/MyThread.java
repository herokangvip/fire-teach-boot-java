package com.example.demo.king.thread;

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
}
