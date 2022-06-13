package com.example.demo.config.db;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * server
 *
 * @author sandykang
 */
@Aspect
@Component
public class DataSourceAop {

    /**
     * slave
     */
    @Pointcut("@annotation(com.example.demo.annotation.Slave)")
    public void readePointcut() {

    }


    /**
     * before
     */
    @Before("readePointcut()")
    public void beforeReade() {
        DBContextHolder.slave();
    }

    /**
     * after
     */
    @After("readePointcut()")
    public void afterReade() {
        DBContextHolder.remove();
    }
}
