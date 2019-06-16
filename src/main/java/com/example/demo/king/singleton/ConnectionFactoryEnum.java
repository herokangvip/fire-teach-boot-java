package com.example.demo.king.singleton;

public enum ConnectionFactoryEnum {
    CONNECTION;
    private Connection connection;

    ConnectionFactoryEnum() {
        connection = new Connection();
        connection.setName("单例");
        connection.setId(1);
    }

    public Connection getConnection() {
        return connection;
    }
}
