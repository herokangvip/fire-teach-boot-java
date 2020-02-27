package com.example.demo.king.thread;

import com.example.demo.king.singleton.Connection;

public enum ConnectionEnum {
    instance;
    private Connection connection;

    ConnectionEnum() {
        connection = new Connection();
    }

    public Connection getConnection() {
        return connection;
    }
}
