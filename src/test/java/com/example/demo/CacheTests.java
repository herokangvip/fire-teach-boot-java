package com.example.demo;

import com.example.demo.domain.User;
import com.example.demo.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {DemoApplication.class})
public class CacheTests {

    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Resource
    private UserService userService;

    @Test
    public void context1() {
        //stringRedisTemplate.opsForHash().put("aa","myKey","MyValue");
        String a = stringRedisTemplate.opsForValue().get("a");
        System.out.println("===:" + a);
        stringRedisTemplate.opsForValue().set("a", "==测试一下=");
        String a2 = stringRedisTemplate.opsForValue().get("a");
        System.out.println("===:" + a2);
        Boolean a1 = stringRedisTemplate.opsForValue().getOperations().delete("a");
        System.out.println("===:" + a1);

    }

    @Test
    public void context2() {
        //stringRedisTemplate.opsForHash().put("aa","myKey","MyValue");
        String key = "testKeyParam";
        String s = userService.testCache(key);
        System.out.println("===:");

    }

    //测试springCache key 包含Date时的情况
    @Test
    public void context3() {
        String s = userService.testCacheDate(new Date(),new User(9,"name","password"));
        System.out.println("===:");
    }

}
