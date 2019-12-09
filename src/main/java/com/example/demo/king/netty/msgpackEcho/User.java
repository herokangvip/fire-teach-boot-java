package com.example.demo.king.netty.msgpackEcho;

import org.msgpack.annotation.Message;

import java.util.ArrayList;
import java.util.List;

@Message
public class User {
    private String name;
    private String age;

    public User() {
    }

    public User(String name, String age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public static List<User> getUsers() {
        ArrayList<User> users = new ArrayList<>();
        users.add(new User("小明", "10"));
        //users.add(new User("冰冰","20"));
        return users;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", age='" + age + '\'' +
                '}';
    }
}
