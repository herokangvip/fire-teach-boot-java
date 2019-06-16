package com.example.demo.king.java8;

/**
 * Created by kangqingqing on 2019/2/18.
 */
public class Context {
    private static ThreadLocal<String> users = new ThreadLocal<>();

    public void set(String s){
        users.set(s);
    }
    public String get(){
        return users.get();
    }

    public void remove(){
        users.remove();
    }

}
