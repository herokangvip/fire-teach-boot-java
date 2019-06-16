package com.example.demo.king.proxy.proxy;

import com.example.demo.king.proxy.JavaCompiler;
import com.squareup.javapoet.*;

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
        FieldSpec jdbcTemplate = FieldSpec.builder(InvocationHandler.class, "invocationHandler", Modifier.PRIVATE).build();
        typeSpec.addField(jdbcTemplate);
        //构造方法
        MethodSpec constructorMethodSpec = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(InvocationHandler.class, "invocationHandler")
                .addStatement("this.invocationHandler = invocationHandler")
                .build();
        typeSpec.addMethod(constructorMethodSpec);
        //增强接口的所有方法(添加执行时间)
        Method[] methods = interfaceClazz.getDeclaredMethods();
        for (Method method : methods) {
            MethodSpec methodSpec = MethodSpec.methodBuilder(method.getName())
                    .addModifiers(Modifier.PUBLIC)
                    .addAnnotation(Override.class)
                    .returns(method.getReturnType())
                    .addParameter(method.getParameterTypes()[0],"param")
                    .addCode("try {\n")
                    .addStatement("\t$T method = " + interfaceClazz.getName() + ".class.getMethod(\"" + method.getName() + "\",Object.class)", Method.class)
                    // 为了简单起见，这里参数直接写死为空
                    .addStatement("\tthis.invocationHandler.invoke(method, param)")
                    .addCode("} catch(Exception e) {\n")
                    .addCode("\te.printStackTrace();\n")
                    .addCode("}\n")
                    .build();
            typeSpec.addMethod(methodSpec);
        }
        // 生成源码文件
        JavaFile javaFile = JavaFile.builder("com.example.demo.proxy.instance", typeSpec.build()).build();
        javaFile.writeTo(new File("D:\\myworkspase\\test\\src\\main\\java"));
        // 编译
        JavaCompiler.compile(new File("D:\\myworkspase\\test\\src\\main\\java\\com\\example\\demo\\proxy\\instance\\MyJdbcTemplate.java"));
        URL[] urls = new URL[]{new URL("file:/D:\\myworkspase\\test\\src\\main\\java\\com\\example\\demo\\proxy\\instance\\MyJdbcTemplate.java")};
        URLClassLoader classLoader = new URLClassLoader(urls);
        Class clazz = classLoader.loadClass("com.example.demo.king.proxy.instance.MyJdbcTemplate");
        Constructor constructor = clazz.getConstructor(InvocationHandler.class);
        Object object = constructor.newInstance(invocation);
        return object;
    }
}
