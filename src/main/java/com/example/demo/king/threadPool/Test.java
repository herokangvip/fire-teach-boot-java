package com.example.demo.king.threadPool;

import java.util.concurrent.*;

public class Test {
    public static void main(String[] args) throws Exception{
        //ExecutorService executorService = Executors.newFixedThreadPool(1);
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(1, 1, 10L, TimeUnit.SECONDS, new LinkedBlockingQueue<>(1));
        threadPoolExecutor.allowsCoreThreadTimeOut();
        threadPoolExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.DiscardPolicy());
        start(threadPoolExecutor, 1);
        System.out.println("启动第1---个线程");

        start(threadPoolExecutor, 2);
        System.out.println("启动第2---个线程");

        start(threadPoolExecutor, 3);
        System.out.println("启动第3---个线程");

        start(threadPoolExecutor, 4);
        System.out.println("启动第4---个线程");
        int activeCount1 = threadPoolExecutor.getActiveCount();


        //Thread.sleep(1000*15L);
        int activeCount2 = threadPoolExecutor.getActiveCount();
        System.out.println("end");
    }

    private static void start(ThreadPoolExecutor threadPoolExecutor, Integer num) {
        //System.out.println("第" + num + "任务执行");
        threadPoolExecutor.submit(() -> {
            try {
                System.out.println("第" + num + "任务执行");
                Thread.sleep(999L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }
}
