package com.example.demo.king.thread;

import com.example.demo.king.singleton.Connection;

public class SingletonFactory {
    private enum DbConnectionEnum {
        factory;
        private Connection connection;
        DbConnectionEnum() {
            //模仿初始化connection的代码
            connection = new Connection();
        }
    }

    private enum HbaseConnectionEnum {
        factory;
        private Connection connection;
        HbaseConnectionEnum() {
            connection = new Connection();
        }
    }

    private enum EsConnectionEnum {
        factory;
        private Connection connection;
        EsConnectionEnum() {
            connection = new Connection();
        }
    }

    public static Connection getDbConnection(){
        return DbConnectionEnum.factory.connection;
    }
    public static Connection getHbaseConnection(){
        return HbaseConnectionEnum.factory.connection;
    }
    public static Connection getEsConnection(){
        return EsConnectionEnum.factory.connection;
    }

    public static void main(String[] args) {
        Connection dbConnection = SingletonFactory.getDbConnection();
        Connection hbaseConnection = SingletonFactory.getHbaseConnection();
        Connection esConnection = SingletonFactory.getEsConnection();
    }
}
