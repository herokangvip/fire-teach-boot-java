package com.example.demo.king.saga;

import java.util.ArrayList;
import java.util.List;

/**
 * saga分布式事务服务类
 * 所有Gaga.service()调用的方法必须在Gaga.begin()和Gaga.begin()中间。
 * xid就是threadid，可以通过Gaga.begin()获取
 */
public class Saga {
    /**
     * 记录事务所有saga节点回滚函数，该对象代替了事件表，每次Gaga分布式事务必须调用Saga.begin()跟Saga.end()；
     */
    static ThreadLocal<List<ICallback<Throwable>>> sagaFailCallBacks = new ThreadLocal<>();

    /**
     * saga事务开始;每次Gaga分布式事务必须调用Saga.begin()跟Saga.end()；
     *
     * @author giant
     * @date 2021年12月15日
     */
    public static long begin() {
        long xid = Thread.currentThread().getId();
        List<ICallback<Throwable>> fails = Saga.sagaFailCallBacks.get();
        if (fails == null) {
            fails = new ArrayList<ICallback<Throwable>>();
            Saga.sagaFailCallBacks.set(fails);
        } else {
            throw new SagaException("程序未关闭saga，请在finally方法执行Saga.end()");
        }
        return xid;
    }


    /**
     * 代理事务节点,该节点执行失败，会根据参数重试，如果方法报错，会回滚其它所有事务的failRollback方法
     * （注意：failRollback方法默认时所有都能执行成功的；建议回滚方法调用Func.proxy retry多次）
     *
     * @param obj          事务节点对象
     * @param retry        事务失败重试次数
     * @param failRollback 如果其中一个service模块失败，将在异常的时候回归，或者调用Gaga.rollback(null)回滚。
     * @return
     * @author giant
     * @date 2021年12月15日
     */
    @SuppressWarnings("unchecked")
    public static <T2> T2 service(T2 obj, int retry, ICallback<Throwable> failRollback) {
        try {
            List<ICallback<Throwable>> fails = Saga.sagaFailCallBacks.get();
            if (fails == null) {
                throw new SagaException("请先开启Saga,调用Saga.begin()");
            }
            fails.add(failRollback);
            Func p = new Func(obj, null, failRollback, retry, true);
            return (T2) p.get();
        } catch (Exception e) {
            throw new SagaException(e);

        }
    }

    /**
     * 代理事务节点，如果报错方法报错，会回滚其它所有事务的failRollback方法
     * （注意：failRollback方法默认时所有都能执行成功的；建议回滚方法调用Func.proxy retry多次）
     *
     * @param obj          事务节点对象
     * @param failRollback
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T2> T2 service(T2 obj, ICallback<Throwable> failRollback) {
        return service(obj, 1, failRollback);
    }

    /**
     * 回滚所有saga执行的链路，在本地方方法抛异常时也可调用，回滚所有方法
     *
     * @param e
     * @date 2021年12月15日
     */
    public static void rollback(Throwable e) {
        List<ICallback<Throwable>> failCallbacks = sagaFailCallBacks.get();
        if (failCallbacks != null) {
            // 这里认为所有方法fail调用都是成功的，如果失败，就是人工处理了。
            failCallbacks.forEach(f -> f.run(e));
            failCallbacks.clear();
        }
        sagaFailCallBacks.set(null);
    }

    /**
     * 结束saga，这个方法是要在saga事务结束的时候调用,调用了Saga.begin()就必须调用该方法
     *
     * @date 2021年12月15日
     */
    public static void end() {
        List<ICallback<Throwable>> fails = Saga.sagaFailCallBacks.get();
        if (fails != null) {
            fails.clear();
        }
        Saga.sagaFailCallBacks.set(null);
    }
}
