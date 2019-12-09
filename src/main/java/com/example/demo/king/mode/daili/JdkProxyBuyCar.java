package com.example.demo.king.mode.daili;


import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class JdkProxyBuyCar implements InvocationHandler {
    private Object object;

    public JdkProxyBuyCar(Object object) {
        this.object = object;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) {
        try {
            method.invoke(object, args);
            System.out.println("帮你上牌---手续费+500");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
