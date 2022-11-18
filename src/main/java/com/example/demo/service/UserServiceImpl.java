package com.example.demo.service;

import com.example.demo.dao.UserMapper;
import com.example.demo.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class UserServiceImpl implements UserService {


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
            log.info("trace日志:{},:{}", "===", "===");

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

    //@Cacheable(cacheManager = "testRedisCache", cacheNames = "cacheNames", key = "#root.targetClass+'['+#key+']'")
    @Cacheable(cacheManager = "testRedisCache", cacheNames = "cacheNames", key = "#key")
    @Override
    public String testCache(String key) {
        return "返回结果";
    }


    @Override
    public User select() {
        return userMapper.select();
    }

    @Override
    public void seeId(List<User> list) {
        for (User user : list) {
            System.out.println("222222=======:" + user.getId());
        }
    }
    @Override
    public void batchInsert(List<User> list) {
        for (User user : list) {
            userMapper.insertSelective(user);
            System.out.println("11111=======:" + user.getId());
        }
    }

    @Cacheable(cacheManager = "testRedisCache", cacheNames = "cacheNames", key = "#root.targetClass+'['+#user+']'")
    @Override
    public String testCacheDate(Date date, User user) {
        return "haha2";
    }
}
