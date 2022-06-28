package com.example.demo;

import com.example.demo.dao.UserMapper;
import com.example.demo.domain.User;
import com.example.demo.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {DemoApplication.class})
public class DbTests {

    @Resource
    private UserMapper userMapper;
    @Resource
    private UserService userService;

    @Test
    public void context1() {
        System.out.println("===:" + "count");
        List<User> list = new ArrayList<>();
        User user1 = new User(null, "user1", "user1");
        User user2 = new User(null, "user1", "user1");
        list.add(user1);
        list.add(user2);

        userService.batchInsert(list);

        userService.seeId(list);
    }



}
