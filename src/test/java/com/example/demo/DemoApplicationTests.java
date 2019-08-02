package com.example.demo;

import com.example.demo.domain.User;
import com.example.demo.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {DemoApplication.class})
//@EnableTransactionManagement
public class DemoApplicationTests {

    @Autowired
    private UserService userService;

    @Test
    public void contextLoads() {
        try {
            User user = new User(null, "name", "pass");
            userService.insert(user);
           /*for (int i = 0; i < 10000000; i++) {
               int insert = userService.insert(user);
                System.out.println(i);
           }*/

            /*new Thread(new Runnable() {
                @Override
                public void run() {
                    userService.update();
                }
            }).start();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    userService.update2();
                }
            }).start();*/
            //int update = userService.update();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

}
