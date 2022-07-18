package com.example.demo.king.cache;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;

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
                    .refreshAfterWrite(100,
                            TimeUnit.SECONDS)
                    .build(key -> load(key));
        } catch (Exception e) {
            System.out.println("=========:error" + e.getMessage());
        }
        //String s = cache.get("a");
        //cache.put("a","b");

        for (int i = 0; i < 1000; i++) {
            Thread.sleep(1000L);
            String s = cache.get("a");
            System.out.println("=====" + s);
        }
        Thread.sleep(10000000L);

    }

    private static String load(String key) {
        System.out.println("update");
        int i = counter.incrementAndGet();
        if (i <= 10) {
            return "ok:"+i;
        } else {
            int a = 1 / 0;
            return a + "";
        }
    }
}
