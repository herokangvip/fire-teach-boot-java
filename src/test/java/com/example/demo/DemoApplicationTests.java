package com.example.demo;

import com.example.demo.dao.UserMapper;
import com.example.demo.domain.User;
import com.example.demo.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.sql.DataSource;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {DemoApplication.class})
//@PropertySource(value = "classpath:properties/${spring.profiles.active}/app.yml", factory = YmlConfigFactory.class,encoding = "utf-8")
public class DemoApplicationTests {

    @Autowired
    private UserService userService;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    @Qualifier(value = "masterDataSource")
    private DataSource master;

    @Autowired
    @Qualifier(value = "slaveDataSource")
    private DataSource slave;



    @Test
    public void context1() {
        User user = userMapper.select();
        System.out.println(user);
    }
}
