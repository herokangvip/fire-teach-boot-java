package com.example.demo.king.cglibProxy;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * @author k
 * @version 1.0
 * @date 2020/1/11 18:00
 */
public class CglibProxy implements MethodInterceptor {
    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        System.out.println("before method run...");
        Object result = methodProxy.invokeSuper(obj, args);
        System.out.println("after method run...");
        return result;
    }
}
