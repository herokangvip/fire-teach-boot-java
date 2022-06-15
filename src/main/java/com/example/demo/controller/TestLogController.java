package com.example.demo.controller;

import com.example.demo.dao.AwardRecordMapper;
import com.example.demo.domain.AwardRecord;
import com.example.demo.service.UserService;
import javafx.css.StyleableLongProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * ce
 * Created by k on 2018/11/15.
 */
//@PrintLog
@Controller
@RequestMapping("/log")
public class TestLogController {

    Logger logger = LoggerFactory.getLogger(TestLogController.class);


    @Resource
    private AwardRecordMapper awardRecordMapper;

    @RequestMapping("/awardRecord")
    @ResponseBody
    public AwardRecord test(Long id) {
        return awardRecordMapper.selectByPrimaryKey(id);
    }


    /**
     * post 也可以用@RequestParam
     */
    @RequestMapping(value = "/testPost", method = RequestMethod.POST)
    @ResponseBody
    public String testPost(@RequestParam Long id, @RequestParam String seqId) {
        System.out.println("testPost:" + id);
        return "awardRecordMapper.selectByPrimaryKey(id)";
    }

    //@PrintLog
    @RequestMapping("/test")
    @ResponseBody
    public String test(String id) {
        logger.info("trace日志:{},:{}", "a", "b");
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
