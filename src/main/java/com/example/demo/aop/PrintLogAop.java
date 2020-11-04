package com.example.demo.aop;

import com.example.demo.controller.TestLogController;
import com.example.demo.log.TraceLog;
import com.example.demo.log.TraceLogHolder;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

/**
 * Created by k on 2018/11/16.
 */
@Configuration//声明这是一个spring管理的配置bean
@Aspect//声明这是一个aop切面类
public class PrintLogAop {
    private static Logger logger = new TraceLog(LoggerFactory.getLogger(TestLogController.class));


    //@Before("execution(* com.example.demo.controller..*.*(..))")
    public void before(JoinPoint joinPoint) {
        System.out.println("==== aop before");
    }

    //@After("execution(* com.example.demo.controller..*.*(..))")
    public void after(JoinPoint joinPoint) {
        System.out.println("==== aop after");
    }

    //@AfterThrowing("execution(* com.example.demo.controller..*.*(..))")
    public void afterThrowing(JoinPoint joinPoint) {
        System.out.println("==== aop afterThrowing");
    }


    // ====类:com.example.demo.controller.MainController,方法:test,入参：[f555]
    // ==== aop before
    // ====类:com.example.demo.controller.MainController,方法:test,出参：Hell World SpringBoot:111
    // ==== aop after
    //@Around("@within(com.example.demo.annotation.PrintLog)")//指定类上标注该注解
    //@Around("@annotation(org.springframework.web.bind.annotation.RequestMapping)")//指定方法上标注该注解
    @Around("execution(* com.example.demo.controller..*.*(..))")//某个包及其子包下所有类的所有方法
    public Object simpleAop(final ProceedingJoinPoint pjp) throws Throwable {
        try {
            TraceLogHolder.put(TraceLogHolder.beginTime, System.currentTimeMillis() + "");
            String methodName = pjp.getSignature().getName();
            String className = pjp.getSignature().getDeclaringType().getName();
            Object[] args = pjp.getArgs();
            //logger.info("begin-args:" + Arrays.toString(args));
            Object proceed = pjp.proceed();
            //logger.info("end-res:" + Arrays.toString(args));
            //logger.info("===耗时:{}ms", System.currentTimeMillis() - Long.parseLong(TraceLogHolder.get(TraceLogHolder.beginTime)));
            TraceLogHolder.remove();
            return proceed;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            throw throwable;
        }
    }

}
