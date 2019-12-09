package com.example.demo.king.myproxy;

import com.example.demo.king.proxy.JdbcTemplate;

import java.lang.reflect.Method;

public class MainTest {
    public static void main(String[] args) throws Exception {
        /*Object proxyInstance = Proxy.newProxyInstance(Connection.class, new JdbcInvocationHandler());
        Connection connection = (Connection) proxyInstance;
        connection.add();
        connection.delete();
        connection.update();
        connection.find();*/
        com.example.demo.king.proxy.JdbcTemplate jdbcTemplate = new com.example.demo.king.proxy.JdbcTemplate();
        Class clazz = JdbcTemplate.class;
        Class[] interfaces = clazz.getInterfaces();
        for (Class interfaceClazz : interfaces) {
            Method[] methods = interfaceClazz.getMethods();
            for (Method methodTemp : methods) {
                String methodName = methodTemp.getName();
                Class<?>[] parameterTypes = methodTemp.getParameterTypes();
                Method method = clazz.getMethod(methodName, parameterTypes);
                System.out.println("方法执行前");
                Object invoke = method.invoke(jdbcTemplate, null);
                System.out.println("方法执行后");
            }

        }

    }
}
