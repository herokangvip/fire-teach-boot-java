package com.example.demo.king.cache;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class CacheTest2 {

    public static void main(String[] args) throws InterruptedException {

        // CacheLoader 初始化
        CacheLoader<String, String> cacheLoader = new CacheLoader<String, String>() {
            @Override
            // load方法的作用是在通过get方法从LoadingCache获取不到值时去加载该值并放入缓存。
            public String load(String key) throws Exception {
                //return null;
                Thread.sleep(10000L);
                return "新值";
            }
        };

        LoadingCache<String, String> cache = CacheBuilder.newBuilder()
                // 设置容量大小
                .maximumSize(5)
                // 设置超时时间
                .refreshAfterWrite(3, TimeUnit.SECONDS)
                // 加载器配置
                .build(cacheLoader);
        cache.put("a","初始值");


        for (int i = 0; i < 1000; i++) {
            String s = null;
            try {
                s = cache.getUnchecked("a");
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
