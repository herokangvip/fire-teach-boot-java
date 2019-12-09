package com.example.demo.king.thread;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ThreadTest2 {
    private static ReentrantLock lock = new ReentrantLock();
    private static Condition conditionA = lock.newCondition();
    private static Condition conditionB = lock.newCondition();

    static class ThreadA extends Thread {
        @Override
        public void run() {
            try {
                lock.lock();
                System.out.println(Thread.currentThread().getName() + "---start");
                conditionA.await();
                System.out.println(Thread.currentThread().getName() + "---end");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }
    }

    static class ThreadB extends Thread {
        @Override
        public void run() {
            try {
                lock.lock();
                System.out.println(Thread.currentThread().getName() + "---start");
                conditionB.await();
                System.out.println(Thread.currentThread().getName() + "---end");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }
    }

    public static void main(String[] args) throws Exception {
        new ThreadA().start();
        new ThreadB().start();
        Thread.sleep(3000L);
        System.out.println("等待3秒后唤醒A线程");
        try {
            lock.lock();
            conditionA.signalAll();
        } finally {
            lock.unlock();
        }
        Thread.sleep(3000L);
        System.out.println("再等待3秒后唤醒B线程");
        try {
            lock.lock();
            conditionB.signalAll();
        } finally {
            lock.unlock();
        }
    }
}
