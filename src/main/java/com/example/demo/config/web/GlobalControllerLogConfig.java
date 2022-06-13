package com.example.demo.config.web;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.MDC;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * ControllerLogAopConfig
 *
 * @author sandykang
 */
@Configuration
@Aspect
@Slf4j
public class GlobalControllerLogConfig {
    //指定类上标注该注解
    //@Around("@within(com.example.demo.annotation.PrintLog)")
    //指定方法上标注该注解
    //@Around("@annotation(org.springframework.web.bind.annotation.RequestMapping)")
    @Around("execution(* com.example.demo.controller..*.*(..))")
    public Object simpleAop(final ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            String methodName = joinPoint.getSignature().getName();
            String className = joinPoint.getSignature().getDeclaringType().getName();
            StringBuilder sb = new StringBuilder();
            sb.append(className).append(".").append(methodName).append(".");
            List<Object> objects = filterArgs(joinPoint.getArgs());
            log.info(sb.toString() + "params:{}", JSONObject.toJSONString(objects));
            TimeInterval timer = DateUtil.timer();
            Object result = joinPoint.proceed(joinPoint.getArgs());
            log.info(sb.toString() + "result:{},ms:{}", JSONObject.toJSONString(result), timer.interval());
            return result;
        } catch (Throwable throwable) {
            //throwable.printStackTrace();
            throw throwable;
        }
    }

    private List<Object> filterArgs(Object[] objects) {
        return Arrays.stream(objects).filter(obj -> !(obj instanceof MultipartFile)
                && !(obj instanceof HttpServletResponse)
                && !(obj instanceof HttpServletRequest))
                .peek(obj -> {
                    if (obj instanceof String) {
                        if ("seqId".equals(obj)) {
                            MDC.put("seqId", String.valueOf(obj));
                        }
                    } else {
                        Map<String, Object> map = new HashMap<>();
                        BeanUtil.beanToMap(obj, map, false, true);
                        if (map.containsKey("seqId")) {
                            MDC.put("seqId", String.valueOf(map.get("seqId")));
                        }
                    }
                })
                .collect(Collectors.toList());
    }

}
