package com.example.demo.king.mode.daili;


import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

public class CglibProxyBuyCar implements MethodInterceptor {
    public Object getProxy(Class<?> clzss) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(clzss);
        // 设置回调方法
        enhancer.setCallback(this);
        // 创建代理对象
        return enhancer.create();
    }

    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        Object result = methodProxy.invokeSuper(o, objects);
        System.out.println("代理买车---手续费+500");
        return result;
    }
}
