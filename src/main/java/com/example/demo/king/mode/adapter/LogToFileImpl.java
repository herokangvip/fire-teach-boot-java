package com.example.demo.king.mode.adapter;

public class LogToFileImpl implements LogToFile {
    @Override
    public void write(String log) {
        System.out.println("日志写到文件");
    }

    @Override
    public void delete(String log) {
        System.out.println("日志从文件删除");
    }

    @Override
    public String read() {
        System.out.println("从文件读取日志");
        return "";
    }
}
