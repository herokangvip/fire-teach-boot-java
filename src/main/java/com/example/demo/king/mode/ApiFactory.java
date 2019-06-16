package com.example.demo.king.mode;

public class ApiFactory {
    public static Api createApi(){
        return new ApiImpl();
    }
}
