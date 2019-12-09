package com.example.demo.king.mode.adapter;

public interface LogToFile {
    void write(String log);

    void delete(String log);

    String read();
}
