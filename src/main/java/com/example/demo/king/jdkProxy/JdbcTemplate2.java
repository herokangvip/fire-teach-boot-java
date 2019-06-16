package com.example.demo.king.jdkProxy;

public class JdbcTemplate2 implements Connection {
    @Override
    public Object add() {
        try {
            Thread.sleep(100L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("add");
        return null;
    }

    @Override
    public void delete() {
        try {
            Thread.sleep(100L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("delete");
    }

    @Override
    public void update() {
        try {
            Thread.sleep(100L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("update");
    }

    @Override
    public void find() {
        try {
            Thread.sleep(100L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("find");
    }
}
