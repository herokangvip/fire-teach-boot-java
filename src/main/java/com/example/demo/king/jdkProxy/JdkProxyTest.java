package com.example.demo.king.jdkProxy;

import java.lang.reflect.Proxy;
import java.util.concurrent.CountDownLatch;

public class JdkProxyTest {
    public static void main(String[] args) throws Exception {
        //sun.misc.ProxyGenerator.saveGeneratedFiles
        //到项目目录下com.sun.proxy下查看生成的代理类class文件
        System.getProperties().put("sun.misc.ProxyGenerator.saveGeneratedFiles", "true");
        Class<JdbcTemplate> clazz = JdbcTemplate.class;
        JdkInvocation jdkInvocation = new JdkInvocation(new JdbcTemplate());
        Connection connection = (Connection) Proxy.newProxyInstance(clazz.getClassLoader(), clazz.getInterfaces(), jdkInvocation);
        connection.add();

        Class<JdbcTemplate2> clazz2 = JdbcTemplate2.class;
        JdkInvocation jdkInvocation2 = new JdkInvocation(new JdbcTemplate2());
        Connection connection2 = (Connection) Proxy.newProxyInstance(clazz2.getClassLoader(), clazz2.getInterfaces(), jdkInvocation2);
        connection2.add();
        System.out.println(connection.getClass());
        System.out.println(connection2.getClass());
        //System.out.println(add1.hashCode());
        //new CountDownLatch(1).await();
    }
}
