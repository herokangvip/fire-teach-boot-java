package com.example.demo.king.mode.adapter;

public class LogToDbImpl implements LogToDb {
    @Override
    public void add(String log) {
        System.out.println("写到数据库");
    }

    @Override
    public void delete(String log) {
        System.out.println("从数据库删除");
    }

    @Override
    public void update(String log) {
        System.out.println("更新到数据库");
    }

    @Override
    public String query() {
        System.out.println("从数据库查询");
        return "";
    }
}
