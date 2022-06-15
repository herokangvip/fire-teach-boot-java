package com.example.demo;

import com.example.demo.dao.UserMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {DemoApplication.class})
public class DbTests {

    @Resource
    private UserMapper userMapper;

    @Test
    public void context1() {
        System.out.println("===:" + "count");

    }


}
