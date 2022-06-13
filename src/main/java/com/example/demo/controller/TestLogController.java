package com.example.demo.controller;

import com.example.demo.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * ce
 * Created by k on 2018/11/15.
 */
//@PrintLog
@Controller
@RequestMapping("/log")
public class TestLogController {
    //Logger logger = new TraceLog(LoggerFactory.getLogger(TestLogController.class));
    Logger logger = LoggerFactory.getLogger(TestLogController.class);


    //@PrintLog
    @RequestMapping("/test")
    @ResponseBody
    public String test(String id) {
        logger.info("trace日志:{},:{}","a","b");
        //int insert = userService.insert(null);
        return "Hell World SpringBoot:111";
    }

    @Autowired
    private UserService userService;

    @RequestMapping("/test2")
    public String test2() {
        System.out.println(userService.getClass());
        return "Hell World SpringBoot:111";
    }



}
