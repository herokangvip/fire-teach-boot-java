package com.example.demo.king.proxy;

import com.example.demo.king.proxy.proxy.JdbcInvocationHandler;
import com.example.demo.king.proxy.proxy.Proxy;

public class MainTest {
    public static void main(String[] args) throws Exception {
        //自定义增强类
        JdbcInvocationHandler handler = new JdbcInvocationHandler(new JdbcTemplate());
        //获取动态代理实例
        Object proxyInstance = Proxy.newProxyInstance(Connection.class, handler);
        Connection connection = (Connection) proxyInstance;
        Object param = new Object();
        //调用方法
        connection.add(param);
        connection.update(param);
    }
}
