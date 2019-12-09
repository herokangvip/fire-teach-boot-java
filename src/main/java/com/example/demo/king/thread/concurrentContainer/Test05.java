package com.example.demo.king.thread.concurrentContainer;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Test05 {
    // [ˈseməfɔːr] 信号量
    private static Semaphore semaphore = new Semaphore(2);
    private static int coreSize = 10;
    private static long timeOut = 10;
    private static LinkedBlockingQueue queue = new LinkedBlockingQueue<>();
    private static TimeUnit timeUnit = TimeUnit.SECONDS;
    private static AtomicInteger index = new AtomicInteger(0);

    public static void main(String[] args) {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(coreSize, coreSize, timeOut, timeUnit, queue);
        long start = System.currentTimeMillis();
        for (int i = 0; i < 8; i++) {
            executor.submit(() -> {
                try {
                    semaphore.acquire();
                    TimeUnit.SECONDS.sleep(1);
                    int name = index.incrementAndGet();
                    System.out.println("第" + name + "个线程执行完毕:" + (System.currentTimeMillis() - start) / 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    semaphore.release();
                }
            });
        }
        executor.shutdown();
    }
}
