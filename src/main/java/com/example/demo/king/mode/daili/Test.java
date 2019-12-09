package com.example.demo.king.mode.daili;


import java.lang.reflect.Proxy;

public class Test {
    public static void main(String[] args) {
        Class<MySelfBuyCar> clazz = MySelfBuyCar.class;
        JdkProxyBuyCar jdkProxyBuyCar = new JdkProxyBuyCar(new MySelfBuyCar());
        BuyCar buyCar = (BuyCar) Proxy.newProxyInstance(clazz.getClassLoader(), clazz.getInterfaces(), jdkProxyBuyCar);
        buyCar.buyCar();

        CglibProxyBuyCar cglibProxyBuyCar = new CglibProxyBuyCar();
        MySelfBuyCar proxy = (MySelfBuyCar) cglibProxyBuyCar.getProxy(MySelfBuyCar.class);
        proxy.buyCar();
    }
}
