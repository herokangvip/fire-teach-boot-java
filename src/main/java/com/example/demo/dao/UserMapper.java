package com.example.demo.dao;

import com.example.demo.domain.User;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserMapper {
    int insert(User record);

    User select();

    int insertSelective(User record);

    int update();

    int update2();
}