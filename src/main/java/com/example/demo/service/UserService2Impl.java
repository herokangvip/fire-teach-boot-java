package com.example.demo.service;

import com.example.demo.dao.UserMapper;
import com.example.demo.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService2Impl implements UserService2 {

    @Autowired
    private UserMapper userMapper;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public int update2() {
        for (int i = 0; i < 1000; i++) {
            userMapper.update2();
        }
        return userMapper.update2();
    }
    @Override
    @Transactional
    public int insert2(User user) {
        int insert = userMapper.insert(user);
        return insert;
    }
}
