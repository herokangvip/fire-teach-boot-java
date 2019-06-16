package com.example.demo.king.proxy.proxy;


import com.example.demo.king.proxy.JdbcTemplate;

import java.lang.reflect.Method;

public class JdbcInvocationHandler implements InvocationHandler {
    private JdbcTemplate jdbcTemplate;

    public JdbcInvocationHandler(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Object invoke(Method method, Object[] args) {
        try {
            long start = System.currentTimeMillis();
            method.invoke(jdbcTemplate, args);
            long end = System.currentTimeMillis();
            System.out.println("耗时：" + (end - start) + "ms");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
