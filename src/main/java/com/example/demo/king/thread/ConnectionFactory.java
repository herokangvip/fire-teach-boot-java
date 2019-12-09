package com.example.demo.king.thread;

import com.example.demo.king.singleton.Connection;

public enum ConnectionFactory {
    factory;
    private Connection connection;

    ConnectionFactory() {
        //模仿初始化connection的代码
        connection = new Connection();
    }

    public Connection getConnection() {
        return connection;
    }

    /**
     * 使用时代码如下
     */
    public static void main(String[] args) throws Exception{
        Connection connection = ConnectionFactory.factory.getConnection();
    }
}
