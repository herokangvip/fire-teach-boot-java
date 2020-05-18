package com.example.demo.log;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TraceLogHolder {

    /**
     * 开始时间，用于打印请求耗时
     */
    public static final String beginTime = "beginTime";

    /**
     * 单次请求日志唯一标记
     */
    public static final String uuid = "uuid";

    public static ThreadLocal<Map<String, String>> context = new ThreadLocal<>();


    public static String getUuid() {
        Map<String, String> map = context.get();
        if (map == null) {
            map = new HashMap<>();
        }
        if (map.get(uuid) == null) {
            String res = UUID.randomUUID().toString();
            put(uuid, res);
            return res;
        }
        return map.get(uuid);
    }

    public static String get(String key) {
        Map<String, String> map = context.get();
        if (map == null) {
            map = new HashMap<>();
        }
        context.set(map);
        return map.get(key);
    }

    public static String put(String key, String value) {
        Map<String, String> map = context.get();
        if (map == null) {
            map = new HashMap<>();
        }
        map.put(key, value);
        context.set(map);
        return map.get(key);
    }

    public static void remove() {
        context.remove();
    }
}
