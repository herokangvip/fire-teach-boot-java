package com.example.demo.king.reactor;

import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * server
 *
 * @author sandykang
 */
public class Test0 {

    public static void main(String[] args) throws Exception {
        ThreadPoolExecutor executor1 = new ThreadPoolExecutor(1, 1,
                1L, TimeUnit.SECONDS, new LinkedBlockingQueue<>(16),
                new ThreadPoolExecutor.CallerRunsPolicy());
        ThreadPoolExecutor executor2 = new ThreadPoolExecutor(1, 1,
                1L, TimeUnit.SECONDS, new LinkedBlockingQueue<>(16),
                new ThreadPoolExecutor.CallerRunsPolicy());
        ThreadPoolExecutor executor3 = new ThreadPoolExecutor(1, 1,
                1L, TimeUnit.SECONDS, new LinkedBlockingQueue<>(16),
                new ThreadPoolExecutor.CallerRunsPolicy());
        ThreadPoolExecutor executor4 = new ThreadPoolExecutor(1, 1,
                1L, TimeUnit.SECONDS, new LinkedBlockingQueue<>(16),
                new ThreadPoolExecutor.CallerRunsPolicy());
        executor1.submit(new Runnable() {
            @Override
            public void run() {
                System.out.println("executor1:" + Thread.currentThread().getName());
            }
        });
        executor2.submit(new Runnable() {
            @Override
            public void run() {
                System.out.println("executor1:" + Thread.currentThread().getName());
            }
        });
        executor3.submit(new Runnable() {
            @Override
            public void run() {
                System.out.println("executor1:" + Thread.currentThread().getName());
            }
        });
        executor4.submit(new Runnable() {
            @Override
            public void run() {
                System.out.println("executor1:" + Thread.currentThread().getName());
            }
        });
        CountDownLatch countDownLatch = new CountDownLatch(1);
        List<String> list = new ArrayList<>();
        Mono.just("1")
                //线程main
                //.publishOn(Schedulers.fromExecutorService(executor1))
                .filter(s -> {
                    System.out.println(Thread.currentThread().getName() + " filter 1");
                    return s.equals("1");
                })
                .publishOn(Schedulers.fromExecutorService(executor1))
                //执行线程：Schedulers.single()
                .filter(s -> {
                    System.out.println(Thread.currentThread().getName() + " filter 2");
                    return s.equals("1");
                })
                //.publishOn(Schedulers.fromExecutorService(executor2))
                //执行线程：newParallel
                .map(s -> {
                    System.out.println(Thread.currentThread().getName() + " map 3");
                    return s.concat("map");
                })
                //.subscribeOn(Schedulers.fromExecutorService(executor3))
                .subscribeOn(Schedulers.fromExecutorService(executor4))
                //subscribe的执行线程，当有多个为schedulers时为前一个Schedulers.single()
                .subscribe(data -> {
                    try {
                        Thread.sleep(10000L);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println(Thread.currentThread().getName() + "subscribe 4:" + data);
                    list.add(data);
                    countDownLatch.countDown();
                });


        System.out.println("main end");
        countDownLatch.await();
        System.out.println("Mono end");

        executor1.shutdown();
        executor2.shutdown();
        executor3.shutdown();
        executor4.shutdown();
    }
}
