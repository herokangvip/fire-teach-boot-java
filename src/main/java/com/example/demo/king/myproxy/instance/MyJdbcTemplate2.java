package com.example.demo.king.myproxy.instance;


import com.example.demo.king.myproxy.Connection;
import com.example.demo.king.myproxy.JdbcTemplate;

public class MyJdbcTemplate2 implements Connection {
    private JdbcTemplate jdbcTemplate;

    public MyJdbcTemplate2(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void add(Object object) {
        long start = System.currentTimeMillis();
        this.jdbcTemplate.add(object);
        long end = System.currentTimeMillis();
        System.out.println(" addTime =" + (end - start));
    }

    @Override
    public void update(Object object) {
        long start = System.currentTimeMillis();
        this.jdbcTemplate.update(object);
        long end = System.currentTimeMillis();
        System.out.println(" updateTime =" + (end - start));
    }

    @Override
    public void find(Long id) {
        long start = System.currentTimeMillis();
        this.jdbcTemplate.find(id);
        long end = System.currentTimeMillis();
        System.out.println(" findTime =" + (end - start));
    }

    @Override
    public void delete(Long id) {
        long start = System.currentTimeMillis();
        this.jdbcTemplate.delete(id);
        long end = System.currentTimeMillis();
        System.out.println(" deleteTime =" + (end - start));
    }
}
