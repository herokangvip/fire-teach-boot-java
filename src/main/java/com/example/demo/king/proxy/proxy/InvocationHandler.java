package com.example.demo.king.proxy.proxy;

import java.lang.reflect.Method;

public interface InvocationHandler {
    Object invoke(Method method, Object... args);
}
