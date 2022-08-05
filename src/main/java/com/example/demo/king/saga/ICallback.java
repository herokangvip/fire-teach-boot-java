package com.example.demo.king.saga;

/**
 * lambda表达式实现，方法成功或者失败的回调接口
 * @param <T> 参数对象
 */
@FunctionalInterface
public interface ICallback<T> {

    public void run(T obj);
}
