package com.example.demo.controller;

import com.example.demo.domain.MyUser;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * ce
 * Created by kangqingqing on 2018/11/15.
 */
@RestController
public class MainController {

    @RequestMapping("/test/{id}")
    public String test(@PathVariable String id){
        return "Hell World SpringBoot:"+id;
    }
    @RequestMapping("/notLogin")
    public String notLogin(){
        return "您还未登录";
    }

    @RequestMapping("/addUser")
    public @ResponseBody String user(@Validated(MyUser.Add.class) MyUser myUser, BindingResult result){
        if(result.hasErrors()){
            List<ObjectError> allErrors = result.getAllErrors();
            for (ObjectError error : allErrors) {
                FieldError err = (FieldError) error;
                System.out.println("addUser has error ---类:" + err.getObjectName() + ",属性:" + err.getField() + ",参数校验:" + err.getDefaultMessage());
            }
            return "error";
        }
        return "success";
    }
    @RequestMapping("/updateUser")
    public @ResponseBody String update(@Validated(MyUser.Update.class) MyUser myUser, BindingResult result){
        if(result.hasErrors()){
            List<ObjectError> allErrors = result.getAllErrors();
            for (ObjectError error : allErrors) {
                FieldError err = (FieldError) error;
                System.out.println("updateUser has error ---类:" + err.getObjectName() + ",属性:" + err.getField() + ",参数校验:" + err.getDefaultMessage());
            }
            return "error";
        }
        return "success";
    }

/*    @RequestMapping("/error")
    public @ResponseBody String error(){
        return "error";
    }*/
}
