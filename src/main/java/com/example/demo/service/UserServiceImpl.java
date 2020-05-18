package com.example.demo.service;

import com.example.demo.controller.TestLogController;
import com.example.demo.dao.UserMapper;
import com.example.demo.domain.User;
import com.example.demo.log.TraceLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

@Service
public class UserServiceImpl implements UserService {

    Logger logger = new TraceLog(LoggerFactory.getLogger(TestLogController.class));



    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserService2 userService2;


    @Autowired
    private UserService userService;

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public int insert(User user) {
        try {
            logger.info("trace日志:{},:{}","===","===");

            if (user == null) {
                Thread.sleep(500L);
                return 0;
            }
            int insert = userMapper.insert(user);
            //this.update();//调用本类中其它添加了事务注解的方法，导致update事务不生效
            userService.update();
            return insert;
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public int update() {
        try {
            return userMapper.update();
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }


    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public int update2() {
        return userMapper.update2();
    }


    @Override
    public User select() {
        return userMapper.select();
    }
}
