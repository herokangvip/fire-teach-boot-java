package com.example.demo.controller;

import com.example.demo.annotation.PrintLog;
import com.example.demo.service.UserService;
import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CountDownLatch;

/**
 * ce
 * Created by k on 2018/11/15.
 */
//@PrintLog
@RestController
public class MainController {

    //kafka服务
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    /**
     * 下单接口
     */
    @RequestMapping("/order")
    public @ResponseBody
    String order(){
        //下单完成模拟发送消息到kafka
        kafkaTemplate.send("test-topic","1");
        return "success";
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
        BloomFilter<Integer> bloomFilter = BloomFilter.create((Funnel<Integer>) (arg0, arg1)
                -> arg1.putInt(arg0), 10000000, 0.001d);
        for (int i = 0; i < 100000000; i++) {
            bloomFilter.put(i);
        }
        System.out.println("====" + bloomFilter.approximateElementCount());
        new CountDownLatch(1).await();

    }
}
