package com.example.demo.king.proxy.cglib;

import com.example.demo.domain.User;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

public class CglibProxy {
    //生成代理对象
    public static Object getProxy(Class clazz, MethodInterceptor interceptor) {
        //new 一个Enhancer对象
        Enhancer enhancer = new Enhancer();
        //指定他的父类(注意这 是实现类，不是接口)
        enhancer.setSuperclass(clazz);
        //指定真正做事情的回调方法
        enhancer.setCallback(interceptor);
        //生成代理类对象
        return enhancer.create();
    }

    public static void main(String[] args) {
        Object proxy = CglibProxy.getProxy(User.class, new MethodInterceptor() {
            @Override
            public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
                return methodProxy.invokeSuper(o, objects);
            }
        });
        Object proxy2= CglibProxy.getProxy(User.class, new MethodInterceptor() {
            @Override
            public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
                return methodProxy.invokeSuper(o, objects);
            }
        });
        System.out.println(proxy);
        System.out.println(proxy2);
        System.out.println(proxy.equals(proxy2));
    }
}
