package com.example.demo.king.myproxy.proxy;

import com.example.demo.king.proxy.Connection;
import com.example.demo.king.proxy.JdbcTemplate;
import com.example.demo.king.proxy.proxy.InvocationHandler;
import com.example.demo.king.proxy.JavaCompiler;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import javax.lang.model.element.Modifier;
import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

public class Proxy {
    public static Object newProxyInstance(Class interfaceClazz, InvocationHandler invocation) throws Exception {
        TypeSpec.Builder typeSpec = TypeSpec.classBuilder("MyJdbcTemplate")
                .addSuperinterface(interfaceClazz).addModifiers(Modifier.PUBLIC);
        //添加属性
        FieldSpec jdbcTemplate = FieldSpec.builder(interfaceClazz, "connection", Modifier.PRIVATE).build();
        typeSpec.addField(jdbcTemplate);
        //构造方法
        MethodSpec constructorMethodSpec = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(interfaceClazz, "connection")
                .addStatement("this.connection = connection")
                .build();
        typeSpec.addMethod(constructorMethodSpec);
        //增强接口的所有方法(添加执行时间)
        Method[] methods = interfaceClazz.getDeclaredMethods();
        for (Method method : methods) {
            MethodSpec methodSpec = MethodSpec.methodBuilder(method.getName())
                    .addModifiers(Modifier.PUBLIC)
                    .addAnnotation(Override.class)
                    .returns(method.getReturnType())
                    .addStatement("long start = $T.currentTimeMillis()", System.class)
                    .addCode("\n")
                    .addStatement("this.connection." + method.getName() + "()")
                    .addCode("\n")
                    .addStatement("long end = $T.currentTimeMillis()", System.class)
                    .addStatement("$T.out.println(\" " + method.getName() + "Time =\" + (end - start))", System.class)
                    .build();
            typeSpec.addMethod(methodSpec);
        }
        JavaFile javaFile = JavaFile.builder("com.example.demo.proxy.instance", typeSpec.build()).build();
        // 生成源码文件
        javaFile.writeTo(new File("D:\\myworkspase\\test\\src\\main\\java"));
        // 编译
        JavaCompiler.compile(new File("D:\\myworkspase\\test\\src\\main\\java\\com\\example\\demo\\proxy\\instance\\MyJdbcTemplate.java"));
        URL[] urls = new URL[] {new URL("file:/D:\\myworkspase\\test\\src\\main\\java\\com\\example\\demo\\proxy\\instance\\MyJdbcTemplate.java")};
        URLClassLoader classLoader = new URLClassLoader(urls);
        Class clazz = classLoader.loadClass("com.example.demo.king.proxy.instance.MyJdbcTemplate");
        Constructor constructor = clazz.getConstructor(Connection.class);
        Object object = constructor.newInstance(new JdbcTemplate());
        return object;
    }
}
