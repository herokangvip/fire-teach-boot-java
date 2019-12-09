package com.example.demo.king.thread.concurrentContainer;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class ConcurrentTest {
    private static Random random = new Random(Integer.MAX_VALUE);
    //线程池线程数量
    private static Integer threadNum = 50;
    //并发任务量
    private static Integer taskNum = 200;
    //数据量
    private static Integer dataNum = 200;

    //static CopyOnWriteArraySet<Integer> concurrentList = new CopyOnWriteArraySet<>();
    private static Set<Integer> concurrentList = Collections.synchronizedSet(new HashSet<>());
    ConcurrentHashMap<String,String> map = new ConcurrentHashMap<>();

    static {
        for (int i = 0; i < dataNum; i++) {
            concurrentList.add(i);
        }
    }

    static class MyArray extends Thread {
        @Override
        public void run() {
            for (int i = 0; i < dataNum; i++) {
                concurrentList.add(random.nextInt());
            }
        }
    }

    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        ThreadPoolExecutor poolExecutor = new CounterThreadPool(threadNum, threadNum, 0L, TimeUnit.SECONDS, new LinkedBlockingQueue<>());
        CounterThreadPool.startTime = start;
        for (int i = 0; i < taskNum; i++) {
            poolExecutor.submit(new MyArray());
        }
        poolExecutor.shutdown();
    }

    static class CounterThreadPool extends ThreadPoolExecutor {
        public static long startTime = 0;
        private AtomicInteger size = new AtomicInteger(0);

        public CounterThreadPool(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
            super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
        }

        @Override
        protected void afterExecute(Runnable r, Throwable t) {
            int i = size.incrementAndGet();
            if (i == taskNum) {
                long end = System.currentTimeMillis();
                System.out.println("耗时：====:" + (end - startTime));
            }
        }
    }
}
