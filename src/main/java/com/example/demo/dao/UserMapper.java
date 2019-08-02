package com.example.demo.dao;

import com.example.demo.domain.User;
import org.springframework.stereotype.Repository;

public interface UserMapper {
    int insert(User record);

    int insertSelective(User record);

    int update();
    int update2();
}