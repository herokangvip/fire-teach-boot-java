package com.example.demo.king.thread;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class LockTest {
    private static AtomicInteger size = new AtomicInteger(0);
    private static AtomicBoolean flag = new AtomicBoolean(false);

    public static void main(String[] args) {
        ReentrantLock lock = new ReentrantLock();
        Condition condition = lock.newCondition();
        MyRunnable runnable = new MyRunnable(lock, condition);
        Thread thread = new Thread(runnable);
        thread.start();
        Thread thread2 = new Thread(runnable);
        thread2.start();
        for (;;) {
            if (size.compareAndSet(2, 3)) {
                try {
                    lock.lock();
                    Thread.sleep(5000L);
                    System.out.println("程序启动5秒后，main执行完毕");
                    condition.signalAll();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    flag.compareAndSet(false, true);
                    lock.unlock();
                }
            }
            if(flag.get()){
                System.out.println("程序执行了一遍跳出循环");
                break;
            }

        }
    }

    static class MyRunnable implements Runnable {
        ReentrantLock lock;
        Condition condition;

        MyRunnable(ReentrantLock lock, Condition condition) {
            this.lock = lock;
            this.condition = condition;
        }

        @Override
        public void run() {
            lock.lock();
            try {
                System.out.println("---" + size.incrementAndGet());
                System.out.println(Thread.currentThread().getName() + ":暂停");
                condition.await();
                System.out.println(Thread.currentThread().getName() + ":执行了");
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }
    }
}
