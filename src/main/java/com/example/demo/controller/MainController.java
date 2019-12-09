package com.example.demo.controller;

import com.example.demo.annotation.PrintLog;
import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnel;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CountDownLatch;

/**
 * ce
 * Created by k on 2018/11/15.
 */
//@PrintLog
@RestController
public class MainController {

    //@PrintLog
    @RequestMapping("/test")
    public String test(String id) {
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
/*    @RequestMapping("/error")
    public @ResponseBody String error(){
        return "error";
    }*/
}
