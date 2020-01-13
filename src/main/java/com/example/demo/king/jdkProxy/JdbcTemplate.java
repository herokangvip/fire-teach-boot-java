package com.example.demo.king.jdkProxy;

public class JdbcTemplate implements Connection {
    @Override
    public Object add() {
        try {
            Thread.sleep(100L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("add1");
        new JdbcTemplate().delete();
        return null;
    }

    @Override
    public void delete() {
        try {
            Thread.sleep(100L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("delete1");
    }

    @Override
    public void update() {
        try {
            Thread.sleep(100L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("update1");
    }

    @Override
    public void find() {
        try {
            Thread.sleep(100L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("find1");
    }
}
