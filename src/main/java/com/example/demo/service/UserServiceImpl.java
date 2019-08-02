package com.example.demo.service;

import com.example.demo.dao.UserMapper;
import com.example.demo.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserService2 userService2;


    @Override
    @Transactional(timeout = 20)
    public int insert(User user) {
        try {
            userMapper.update();

            int insert = userMapper.insert(user);
            Thread.sleep(1000L);
            Thread.sleep(1000L);

            return 1;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    @Override
    @Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
    public int update() {
        userMapper.update();
        userService2.update2();
        return userMapper.update();
    }
    @Override
    @Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
    public int update2() {
        return userMapper.update2();
    }
}
