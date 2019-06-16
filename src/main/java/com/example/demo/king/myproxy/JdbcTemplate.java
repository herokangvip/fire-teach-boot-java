package com.example.demo.king.myproxy;

public class JdbcTemplate implements Connection {
    @Override
    public void add(Object object) {
        System.out.println("add");
    }

    @Override
    public void delete(Long id) {
        System.out.println("delete");
    }

    @Override
    public void update(Object object) {
        System.out.println("update");
    }

    @Override
    public void find(Long id) {
        System.out.println("find");
    }
}
