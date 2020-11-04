package com.example.demo.controller.kafka;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/kafka")
public class Atest {


    @RequestMapping("/test")
    @ResponseBody
    public String test(){
        return "aaa";
    }
}
