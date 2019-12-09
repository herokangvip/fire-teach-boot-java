package com.example.demo.king.thread;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ReentrantReadWriteLockTest {
    private static ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    static class ThreadA extends Thread {
        @Override
        public void run() {
            try {
                lock.readLock().lock();
                System.out.println(Thread.currentThread().getName() + ":0:" + System.currentTimeMillis());
                Thread.sleep(1000L);
                System.out.println(Thread.currentThread().getName() + ":1:" + System.currentTimeMillis());
            } catch (InterruptedException e) {
                System.out.println(Thread.currentThread().getName() + "---响应中断");
            } finally {
                lock.readLock().unlock();
            }
        }
    }

    static class ThreadB extends Thread {
        @Override
        public void run() {
            try {
                lock.writeLock().lock();
                System.out.println(Thread.currentThread().getName() + ":0:" + System.currentTimeMillis());
                Thread.sleep(1000L);
                System.out.println(Thread.currentThread().getName() + ":1:" + System.currentTimeMillis());
            } catch (InterruptedException e) {
                System.out.println(Thread.currentThread().getName() + "---响应中断");
            } finally {
                lock.writeLock().unlock();
            }
        }
    }

    public static void main(String[] args) throws Exception {
        ThreadA threadA = new ThreadA();
        threadA.setName("读");
        threadA.start();

        ThreadB threadB = new ThreadB();
        threadB.setName("写");
        threadB.start();
    }
}
