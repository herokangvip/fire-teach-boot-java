package com.example.demo;

import com.example.demo.config.YmlConfigFactory;
import com.example.demo.dao.TestDao;
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

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {DemoApplication.class})
@PropertySource(value = "classpath:properties/${spring.profiles.active}/app.yml",factory = YmlConfigFactory.class,encoding = "utf-8")

public class DemoApplicationTests {

    @Autowired
    private UserService userService;

    @Test
    public void contextLoads() {
        try {
            User user = new User(null, "name", "pass");
            userService.insert(user);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    @Autowired
    private TestDao testDao;

    @Test
    public void test() {
        com.example.demo.domain.Test test = testDao.selectByPrimaryKey(1);
        System.out.println("sss");
    }

}
