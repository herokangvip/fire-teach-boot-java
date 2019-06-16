package com.example.demo.king.mode;

public class Client {
    public static void main(String[] args) {
        Api api = ApiFactory.createApi();
        api.test();
    }
}
