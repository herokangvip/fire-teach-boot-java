package com.example.demo.king.thread.pool;

import java.util.concurrent.*;

public class ThreadPoolTest {
    public static void main(String[] args) throws Exception {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(3, 3,
                1L, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(),
                new ThreadPoolExecutor.DiscardPolicy());
        long start = System.currentTimeMillis();

        FutureTask<String> futureTask = new FutureTask<>(new Callable<String>() {
            @Override
            public String call() throws Exception {
                Thread.sleep(1000L);
                return "远程调用耗时的方法";
            }
        });

        executor.submit(futureTask);
        Thread.sleep(1000L);
        Object s = futureTask.get();
        System.out.println(s);
        long end = System.currentTimeMillis();
        System.out.println("共耗时:" + (end - start) + "ms");
        executor.shutdown();
    }
}
