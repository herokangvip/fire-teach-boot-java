package com.example.demo.king.saga;

/**
 * lambda表达式实现，方法失败重试接口
 * @param <T> 返回对象
 */
@FunctionalInterface
interface IRetry<T>{

    public T run() throws Throwable;
}
