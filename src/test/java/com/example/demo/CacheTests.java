package com.example.demo;

import com.example.demo.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

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

}
