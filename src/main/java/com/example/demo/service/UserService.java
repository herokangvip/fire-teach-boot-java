package com.example.demo.service;

import com.example.demo.domain.User;

import java.util.List;

public interface UserService {
    int insert(User user);

    User select();

    int update();

    int update2();

    String testCache(String key);


    public void seeId(List<User> list);
    public void batchInsert(List<User> list);
}
