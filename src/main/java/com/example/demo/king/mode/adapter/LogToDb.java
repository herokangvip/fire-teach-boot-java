package com.example.demo.king.mode.adapter;

public interface LogToDb {
    void add(String log);

    void delete(String log);

    void update(String log);

    String query();
}
