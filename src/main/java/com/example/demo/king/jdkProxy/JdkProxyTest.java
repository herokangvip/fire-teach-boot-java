package com.example.demo.king.jdkProxy;

import java.lang.reflect.Proxy;
import java.util.concurrent.CountDownLatch;

public class JdkProxyTest {
    public static void main(String[] args) throws Exception{
        //sun.misc.ProxyGenerator.saveGeneratedFiles
        System.getProperties().put("sun.misc.ProxyGenerator.saveGeneratedFiles", "true");
        Class<JdbcTemplate> clazz = JdbcTemplate.class;
        Connection connection = (Connection)Proxy.newProxyInstance(clazz.getClassLoader(), clazz.getInterfaces(), new JdkInvocation());
        connection.add();

        Class<JdbcTemplate2> clazz2 = JdbcTemplate2.class;
        Connection connection2 = (Connection)Proxy.newProxyInstance(clazz2.getClassLoader(), clazz2.getInterfaces(), new JdkInvocation());
        connection2.add();
        System.out.println(connection.getClass());
        System.out.println(connection2.getClass());
        //System.out.println(add1.hashCode());
        new CountDownLatch(1).await();
    }
}
