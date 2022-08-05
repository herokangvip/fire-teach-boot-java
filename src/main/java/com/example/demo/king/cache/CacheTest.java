package com.example.demo.king.cache;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class CacheTest {
    private static AtomicInteger counter = new AtomicInteger(0);

    public static void main(String[] args) throws InterruptedException {

        LoadingCache<String, String> cache = null;
        try {
            cache = Caffeine.newBuilder()
                    .initialCapacity(10)
                    .refreshAfterWrite(10, TimeUnit.SECONDS)
                    //.expireAfterWrite(10, TimeUnit.SECONDS)
                    .build(key -> load(key));
        } catch (Exception e) {
            System.out.println("=========:error" + e.getMessage());
        }
        String s1 = cache.get("a");
        System.out.println("s1:" + s1);
        Thread.sleep(12000);
        String s2 = cache.get("a");
        System.out.println("s2:" + s2);

        //cache.put("a","b");

        for (int i = 0; i < 1000; i++) {
            Thread.sleep(1000L);
            String s = cache.get("a");
            System.out.println("=====" + s);
        }
        Thread.sleep(10000000L);

    }

    private static String load(String key) throws InterruptedException {
        System.out.println("update");
        Thread.sleep(10000L);
        int i = counter.incrementAndGet();
        if (i <= 10) {
            return "ok:" + i;
        } else {
            int a = 1 / 0;
            return a + "";
        }
    }
}
