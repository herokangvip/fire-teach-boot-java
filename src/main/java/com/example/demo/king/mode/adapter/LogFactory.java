package com.example.demo.king.mode.adapter;

public class LogFactory {
    static LogToDb createLogger() {
        return new LogAdapter();
        //可以改为根据配置文件判断使用第一版File版本还是新的DB版本
        //return new LogToDbImpl();
    }
}
