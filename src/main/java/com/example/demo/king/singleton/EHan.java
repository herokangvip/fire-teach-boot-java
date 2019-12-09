package com.example.demo.king.singleton;

import java.util.Vector;

public class EHan {
    private static Connection connection = new Connection();

    private EHan() {
    }

    public static Connection getConnection() {
        return connection;
    }


    public void vectorTest() {
        Vector<String> vector = new Vector<String>();
        for (int i = 0; i < 10; i++) {
            vector.add(i + "");
        }
        System.out.println(vector);
    }
}
