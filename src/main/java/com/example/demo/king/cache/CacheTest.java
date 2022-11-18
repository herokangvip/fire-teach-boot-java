package com.example.demo.king.cache;

import cn.hutool.core.lang.Tuple;
import cn.hutool.core.util.HashUtil;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.google.common.io.Resources;
import org.junit.Assert;

import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.time.LocalDateTime;
import java.time.ZoneId;
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
                    .refreshAfterWrite(2, TimeUnit.SECONDS)
                    //.expireAfterWrite(2, TimeUnit.SECONDS)
                    .build(key -> load(key));
        } catch (Exception e) {
            System.out.println("=========:error" + e.getMessage());
        }
        cache.put("a","初始值");


        for (int i = 0; i < 1000; i++) {
            String s = null;
            try {
                s = cache.get("a");
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println("=====" + s);
            Thread.sleep(1000L);
        }
        Thread.sleep(10000000L);

    }

    private static String load(String key) throws InterruptedException {
        //throw new RuntimeException("测试异常");
        //Thread.sleep(10000L);
        //return "新值";
        return null;
    }
}
