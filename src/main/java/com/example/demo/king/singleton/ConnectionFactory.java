package com.example.demo.king.singleton;

public class ConnectionFactory {
    private static volatile Connection connection;

    private ConnectionFactory() {
    }

    public static Connection getConnection() {
        if (connection == null) {
            synchronized (Connection.class) {
                if (connection == null) {
                    connection = new Connection();
                    connection.setId(1);
                    connection.setName("单例");
                }
            }
        }
        return connection;
    }
}
