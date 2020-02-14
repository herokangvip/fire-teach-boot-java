package com.example.demo.controller;

import com.example.demo.annotation.PrintLog;
import com.example.demo.dao.OrderGroupMapper;
import com.example.demo.dao.OrderTotalMapper;
import com.example.demo.domain.OrderGroup;
import com.example.demo.domain.OrderTotal;
import com.example.demo.domain.vo.OrderTotalVo;
import com.example.demo.domain.vo.OrderVo;
import com.example.demo.service.UserService;
import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnel;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.concurrent.CountDownLatch;

/**
 * ce
 * Created by k on 2018/11/15.
 */
//@PrintLog
@Controller
public class MainController {

    //kafka服务
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;


    /**
     * 下单接口
     */
    @RequestMapping("/order")
    public
    @ResponseBody
    String order() throws InterruptedException {
        //下单完成模拟发送消息到kafka
//        1, 7282164761, 0.1
//
//        1, 7282164774, 1.14
//        1, 7282164762, 0.2
//        1, 7282164763, 0.3
//        1, 7282164764, 0.4
        //1, 7282164765, 0.5
//        1, 7282164766, 0.6
//        1, 7282164767, 0.7
//        1, 7282164768, 0.8
//        1, 7282164769, 0.9
//        1, 7282164770, 1.10
//        1, 7282164771, 1.11
//        1, 7282164772, 1.12
//        1, 7282164773, 1.13
        //kafkaTemplate.send("test-topic","1");
        kafkaTemplate.send("test-topic", "1, 7282164761, 0.1");
        Thread.sleep(300L);
        kafkaTemplate.send("test-topic", "1, 7282164770, 1.10");
        Thread.sleep(300L);
        kafkaTemplate.send("test-topic", "1, 7282164771, 1.11");
        Thread.sleep(300L);
        kafkaTemplate.send("test-topic", "1, 7282164772, 1.12");
        Thread.sleep(300L);
        kafkaTemplate.send("test-topic", "1, 7282164773, 1.13");
        Thread.sleep(300L);
        kafkaTemplate.send("test-topic", "1, 7282164774, 1.14");
        Thread.sleep(300L);
        kafkaTemplate.send("test-topic", "1, 7282164775, 1.15");
        Thread.sleep(300L);
        kafkaTemplate.send("test-topic", "1, 7282164762, 0.2");
        Thread.sleep(300L);
        kafkaTemplate.send("test-topic", "1, 7282164763, 0.3");
        Thread.sleep(300L);
        kafkaTemplate.send("test-topic", "1, 7282164764, 0.4");
        Thread.sleep(300L);
        kafkaTemplate.send("test-topic", "1, 7282164765, 0.5");
        Thread.sleep(300L);
        kafkaTemplate.send("test-topic", "1, 7282164766, 0.6");
        Thread.sleep(300L);
        kafkaTemplate.send("test-topic", "1, 7282164767, 0.7");
        Thread.sleep(300L);
        kafkaTemplate.send("test-topic", "1, 7282164768, 0.8");
        Thread.sleep(300L);
        kafkaTemplate.send("test-topic", "1, 7282164769, 0.9");
        Thread.sleep(300L);
        kafkaTemplate.send("test-topic", "1, 7282164779, 1.19");
        Thread.sleep(300L);
        kafkaTemplate.send("test-topic", "1, 7282164799, 1.99");

        return "success";
    }

    /**
     * 发送kafka接口
     */
    @RequestMapping("/send")
    public
    @ResponseBody
    String send(String value) throws InterruptedException {
        //kafkaTemplate.send("test-topic", "1, 7282164761, 0.1");
        kafkaTemplate.send("test-topic", value);

        return "seccess";
    }


    //@PrintLog
    @RequestMapping("/test")
    public String test(String id) {
        return "Hell World SpringBoot:111";
    }

    @Autowired
    private UserService userService;

    @RequestMapping("/test2")
    public String test2() {
        System.out.println(userService.getClass());
        return "Hell World SpringBoot:111";
    }


    public static void main(String[] args) throws InterruptedException {

    }
}
