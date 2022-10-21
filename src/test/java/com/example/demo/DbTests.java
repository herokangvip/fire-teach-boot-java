package com.example.demo;

import com.example.demo.dao.AwardRecordMapper;
import com.example.demo.dao.UserMapper;
import com.example.demo.dao.WelfareUserTaskMapper;
import com.example.demo.domain.User;
import com.example.demo.domain.WelfareUserTask;
import com.example.demo.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {DemoApplication.class})
public class DbTests {

    @Resource
    private UserMapper userMapper;
    @Resource
    private UserService userService;
    @Resource
    private WelfareUserTaskMapper welfareUserTaskMapper;


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

    @Test
    public void context2() {
        List<Long> list = new ArrayList<>();
        list.add(1L);
        list.add(2L);
        list.add(3L);
        List<WelfareUserTask> welfareUserTasks = welfareUserTaskMapper
                .selectByUserIdAndTaskIdsAndSign("1", list, "1");
        System.out.println("===:" + "count");
    }


    @Resource
    private AwardRecordMapper awardRecordMapper;
    @Test
    public void context3() {
        int count = awardRecordMapper.countByUserIdAndActivityCodeToday("1", new Date(1),
                new Date(System.currentTimeMillis()+100000));
        System.out.println("===:" + "count");
    }

    @Test
    public void context4() {
        List<User> list = new ArrayList<>();
        User user1 = new User(null, "user3", "user1");
        User user2 = new User(null, "user4", "user2");
        list.add(user1);
        list.add(user2);
        int i = userMapper.batchInsert(list);
        System.out.println("===:" + i);
    }

}
