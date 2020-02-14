package com.example.demo;

import com.example.demo.config.YmlConfigFactory;
import com.example.demo.dao.OrderGroupMapper;
import com.example.demo.domain.OrderGroup;
import com.example.demo.domain.User;
import com.example.demo.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {DemoApplication.class})
@PropertySource(value = "classpath:properties/${spring.profiles.active}/app.yml", factory = YmlConfigFactory.class, encoding = "utf-8")

public class DemoApplicationTests {

    @Autowired
    private UserService userService;

    @Test
    public void contextLoads() {

        User user = new User(null, "name", "pass");
        userService.insert(user);
    }


    @Autowired
    private OrderGroupMapper orderGroupMapper;

    @Test
    public void order() {
        List<OrderGroup> list = orderGroupMapper.select(20200208);
        System.out.println("-");
    }
}
