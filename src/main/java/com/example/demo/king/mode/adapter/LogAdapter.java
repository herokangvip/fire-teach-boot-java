package com.example.demo.king.mode.adapter;

public class LogAdapter implements LogToDb {
    private LogToFile logToFile = new LogToFileImpl();

    @Override
    public void add(String log) {
        logToFile.write(log);
    }

    @Override
    public void delete(String log) {
        logToFile.delete(log);
    }

    @Override
    public void update(String log) {
        logToFile.delete(log);
        logToFile.write(log);
    }

    @Override
    public String query() {
        return logToFile.read();
    }
}
