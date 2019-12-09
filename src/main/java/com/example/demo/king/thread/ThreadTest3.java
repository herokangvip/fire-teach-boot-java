package com.example.demo.king.thread;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ThreadTest3 {
    private static ReentrantLock lock = new ReentrantLock();
    private static Condition condition = lock.newCondition();

    static class ThreadA extends Thread {
        @Override
        public void run() {
            try {
                if (lock.tryLock(10L, TimeUnit.SECONDS)) {
                    System.out.println(Thread.currentThread().getName() + "---start");
                    condition.awaitUninterruptibly();
                    System.out.println(Thread.currentThread().getName() + "---end");
                } else {
                    System.out.println(Thread.currentThread().getName() + "---没有获取到锁");
                }
            } catch (InterruptedException e) {
                System.out.println(Thread.currentThread().getName() + "---响应中断");
            } finally {
                if (lock.isHeldByCurrentThread()) {
                    lock.unlock();
                }
            }
        }
    }

    public static void main(String[] args) throws Exception {
        ThreadA thread = new ThreadA();
        thread.start();
        Thread.sleep(1000L);
        thread.interrupt();

    }
}
