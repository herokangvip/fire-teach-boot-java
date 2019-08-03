package com.example.demo.service;

import com.example.demo.domain.User;

public interface UserService {
    int insert(User user);
    User select();
    int update();
    int update2();
}
