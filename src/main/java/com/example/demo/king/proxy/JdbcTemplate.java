package com.example.demo.king.proxy;

public class JdbcTemplate implements Connection {
    @Override
    public void add(Object object) {
        try {
            Thread.sleep(100L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("add");
    }


    @Override
    public void update(Object object) {
        try {
            Thread.sleep(100L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("update");
    }


}
