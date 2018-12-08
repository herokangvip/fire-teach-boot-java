package com.example.demo.domain;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * Created by kangqingqing on 2018/11/16.
 */
public class MyUser {


    @NotNull(groups = {Update.class})
    public Integer id;
    @NotBlank(groups = {Add.class,Update.class})
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
