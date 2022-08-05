package com.example.demo.king.saga;

import java.lang.reflect.Method;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;


/**
 * * 方法执行动态代理，实现代理类方法重试，方法调用成功或者失败后的反调函数实现
 * 代理对象调用方法时，方法与方法直接可嵌套使用。
 */
public class Func implements MethodInterceptor {

    /**
     * 是否saga模式，如果是saga模式，建在报错的时候统一执行Saga.sagaFailCallBacks集合里面的方法
     */
    private boolean isSaga = false;

    private ICallback<Throwable> fail = null;
    private ICallback<Object> success = null;
    private int retry = 1;
    private Object obj = null;


    Func(Object obj, ICallback<Object> success, ICallback<Throwable> fail, int retry, boolean isSaga) {
        // TODO Auto-generated constructor stub
        this.obj = obj;
        this.success = success;
        this.fail = fail;
        this.retry = retry;
        this.isSaga = isSaga;
    }


    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        Object o2 = retry(() -> {
            Object o1 = null;
            try {
                o1 = methodProxy.invokeSuper(o, objects);
                return o1;
            } catch (Throwable e) {
                throw e;
            }
//          return null;
        }, Object.class, this.retry, this.success, this.fail);
        return o2;
    }

    /**
     * 失败重试三次
     *
     * @param run
     * @param clz
     * @return
     */
    private <T> T retry(IRetry<T> run, Class<T> clz, int count,
                        ICallback<Object> success, ICallback<Throwable> fail) throws Throwable {
        int i = 0;
        T result = null;
        boolean isSuccess = false;
        do {
            try {
                result = run.run();
                isSuccess = true;
                if (success != null) {
                    success.run(result);
                }
            } catch (Throwable e) {
                if (fail != null && !isSaga) {
                    fail.run(e);
                }
                if (i == retry - 1) {
                    //向外抛出的异常只认最后一次
                    if (isSaga) {//如果是saga处理，统一最后回滚事务
                        Saga.rollback(e);
                    }
                    throw e;
                }
            }
        } while ((!isSuccess) && ++i < count);
        return result;
    }

    //生成代理对象
    Object get() {
        //new 一个Enhancer对象
        Enhancer enhancer = new Enhancer();
        //指定他的父类(注意这 是实现类，不是接口)
        enhancer.setSuperclass(obj.getClass());
        //指定真正做事情的回调方法
        enhancer.setCallback(this);
        //生成代理类对象
        Object o = enhancer.create();
        //返回
        return o;
    }

    /**
     * 给代理对象赋能，支持方法重试、成功及失败的回调
     *
     * @param obj     代理对象
     * @param retry   代理对象失败重试次数
     * @param success 代理对象所有方法成功回调函数（不报异常就算成功）
     * @param fail    代理对象所有方法失败回调函数。（报异常就算失败）
     * @return 返回代理
     * @author giant
     * @date 2021年12月15日
     */
    @SuppressWarnings("unchecked")
    public static <T2> T2 proxy(T2 obj, int retry, ICallback<Object> success, ICallback<Throwable> fail) {
        try {
            Func p = new Func(obj, success, fail, retry, false);
            return (T2) p.get();
        } catch (Exception e) {
            throw new RuntimeException(e);

        }
    }

    /**
     * 给代理对象赋能，支持方法成功及失败的回调
     *
     * @param obj     代理对象
     * @param success 代理对象所有方法成功回调函数（不报异常就算成功）
     * @param fail    代理对象所有方法失败回调函数（报异常就算失败）
     * @return 返回代理
     * @author giant
     * @date 2021年12月15日
     */
    public static <T2> T2 proxy(T2 obj, ICallback<Object> success, ICallback<Throwable> fail) {
        return proxy(obj, 1, success, fail);
    }


}
