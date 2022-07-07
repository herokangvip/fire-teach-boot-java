package com.example.demo.controller;

import com.example.demo.config.web.CustomJacksonSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;

/**
 * ce
 * Created by k on 2018/11/15.
 */
//@PrintLog
@Controller
@RequestMapping("/json")
public class TestJsonController {

    Logger logger = LoggerFactory.getLogger(TestJsonController.class);


    /**
     * post 也可以用@RequestParam
     */
    @RequestMapping(value = "/test")
    @ResponseBody
    public TestJson test(@RequestBody TestJson testJson) {
        System.out.println("========:" + testJson.getStartTime());
        return new TestJson(new Date());
    }


    private static class TestJson {
        //@JsonFormat(pattern = "yyyy-MM-dd'T' HH:mm:ss:SSS'Z'",timezone = "GMT+8")
        @JsonSerialize(using = CustomJacksonSerializer.DateSerializer.class)
        @JsonDeserialize(using = CustomJacksonSerializer.DateDeserializer.class)
        private Date startTime;

        public TestJson() {
        }

        public TestJson(Date startTime) {
            this.startTime = startTime;
        }

        public Date getStartTime() {
            return startTime;
        }

        public void setStartTime(Date startTime) {
            this.startTime = startTime;
        }
    }
}
