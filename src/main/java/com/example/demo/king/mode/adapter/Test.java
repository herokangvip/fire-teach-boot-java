package com.example.demo.king.mode.adapter;

public class Test {
    public static void main(String[] args) {
        //老版本，日志写到文件
        LogToFile logToFile = new LogToFileImpl();
        logToFile.write("error");
        String read = logToFile.read();
        logToFile.delete("aaa");

        //新版本写到数据库，并提供增删改查功能
        LogToDb logToDb = new LogToDbImpl();
        logToDb.add("aaa");
        logToDb.update("aaa");
        logToDb.delete("aaa");
        String query = logToDb.query();

        LogAdapter adapter = new LogAdapter();
        adapter.add("调用的写到db的api方法，其实底层是写到文件");

        LogToDb logger = LogFactory.createLogger();
        logger.add("a");
        String query1 = logger.query();
    }
}
