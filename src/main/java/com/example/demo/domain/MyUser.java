package com.example.demo.domain;


import com.sun.istack.internal.NotNull;

/**
 * Created by k on 2018/11/16.
 */
public class MyUser {


    public Integer id;
    public String name;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public interface Add {
    }

    public interface Update {
    }
}
