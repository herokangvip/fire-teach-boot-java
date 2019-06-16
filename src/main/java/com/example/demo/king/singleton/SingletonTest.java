package com.example.demo.king.singleton;

public class SingletonTest {
    public static void main(String[] args) {
        int key = StatusEnum.START.getKey();
        String value = StatusEnum.START.getValue();
        System.out.println(key + ":" + value);
        Connection connection = ConnectionFactory.getConnection();
        System.out.println(connection.toString());
        Connection connection1 = ConnectionFactoryEnum.CONNECTION.getConnection();
        System.out.println(connection1.toString());

    }
}
