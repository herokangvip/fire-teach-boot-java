package com.example.demo.king.thread;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class SynchronizedTest {
    private static final Object lock = new Object();
    private static AtomicInteger size = new AtomicInteger(0);
    private static AtomicBoolean flag = new AtomicBoolean(false);

    public static void main(String[] args) {

        Service runnable = new Service();
        Thread thread = new Thread(runnable);
        thread.start();
        Thread thread2 = new Thread(runnable);
        thread2.start();
        for (; ; ) {
            if (size.compareAndSet(2, 3)) {
                synchronized (lock) {
                    try {
                        Thread.sleep(5000L);
                        System.out.println("程序启动5秒后，main执行完毕");
                        lock.notifyAll();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }finally {
                        flag.compareAndSet(false, true);
                    }
                }
            }
            if (flag.get()) {
                System.out.println("程序执行了一遍跳出循环");
                break;
            }

        }
    }

    static class Service implements Runnable {

        @Override
        public void run() {
            synchronized (lock) {
                try {
                    System.out.println("---" + size.incrementAndGet());
                    System.out.println(Thread.currentThread().getName() + ":暂停");
                    lock.wait();
                    System.out.println(Thread.currentThread().getName() + ":执行了");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
