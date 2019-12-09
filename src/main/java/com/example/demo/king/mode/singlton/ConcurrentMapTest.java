package com.example.demo.king.mode.singlton;

import java.util.concurrent.ConcurrentHashMap;

public class ConcurrentMapTest {
    public static void main(String[] args) {
        ConcurrentHashMap<Object, Object> map = new ConcurrentHashMap<>();
        Fat aaa = new Fat("1", "aaa");
        Fat bbb = new Fat("1", "aaa");
        System.out.println(aaa.equals(bbb));
        map.put(aaa, "1");
        System.out.println(map.get(bbb));
    }
}
