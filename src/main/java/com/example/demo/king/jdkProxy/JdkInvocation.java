package com.example.demo.king.jdkProxy;


import java.lang.reflect.Method;

public class JdkInvocation implements java.lang.reflect.InvocationHandler {
    private Connection template;

    public JdkInvocation(Connection template) {
        this.template = template;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) {
        try {
            System.out.println("jdk动态代理增强");
            Object invoke = method.invoke(template, args);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return proxy;
    }
}
