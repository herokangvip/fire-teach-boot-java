package com.example.demo.king.mode.singlton;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Test {
    private static final Map<Object, Object> map = new HashMap<>();

    public static void main(String[] args) throws Exception {
        ReferenceQueue queue = new ReferenceQueue();


        WeakReference<byte[]> reference1 = new WeakReference<>(new byte[1024 * 1024 * 3],queue);
        WeakReference<byte[]> reference2 = new WeakReference<>(new byte[1024 * 1024 * 3],queue);
        WeakReference<byte[]> reference3 = new WeakReference<>(new byte[1024 * 1024 * 3],queue);

        map.put(reference1, 1);
        map.put(reference2, 2);
        map.put(reference3, 3);
        WeakReference<byte[]> reference4 = new WeakReference<>(new byte[1024 * 1024 * 3],queue);
        map.put(reference4, 4);

        SoftReference<byte[]> reference11 = new SoftReference<>(new byte[1024 * 1024 * 3],queue);
        SoftReference<byte[]> reference12 = new SoftReference<>(new byte[1024 * 1024 * 3],queue);
        SoftReference<byte[]> reference13 = new SoftReference<>(new byte[1024 * 1024 * 3],queue);
        SoftReference<byte[]> reference14 = new SoftReference<>(new byte[1024 * 1024 * 3],queue);
        SoftReference<byte[]> reference15 = new SoftReference<>(new byte[1024 * 1024 * 3],queue);
        SoftReference<byte[]> reference16 = new SoftReference<>(new byte[1024 * 1024 * 3],queue);
        SoftReference<byte[]> reference17 = new SoftReference<>(new byte[1024 * 1024 * 3],queue);
        SoftReference<byte[]> reference18 = new SoftReference<>(new byte[1024 * 1024 * 3],queue);
        SoftReference<byte[]> reference19 = new SoftReference<>(new byte[1024 * 1024 * 3],queue);




        //System.out.println(map.size());

        Thread.sleep(1000L);

        System.out.println("map的key:value值====");
        for (Map.Entry<Object,Object> entry: map.entrySet()) {
            System.out.println(entry.getKey()+":"+entry.getValue());
        }


        Thread thread = new Thread() {
            @Override
            public void run() {
                System.out.println("回收情况====");
                try {
                    Reference k;
                    while ((k = queue.remove()) != null) {
                        Object remove = map.remove(k);
                        System.out.println(remove + "被回收了");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();


        System.out.println("引用对象的值====");
        System.out.println(reference1.get());
        System.out.println(reference2.get());
        System.out.println(reference3.get());
        System.out.println(reference4.get());


        Thread.sleep(1000L);
        System.out.println("回收后map的size===="+map.size());


    }
}
