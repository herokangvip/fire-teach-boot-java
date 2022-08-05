package com.example.demo.king.saga;

public class SagaTest {

    public static void main(String[] args) {
        new SagaTest().methodA();
    }

    public void testFunc() {
        System.out.println("**********先测试 Func方法测试 开始***********");
        String failStr = "调用失败";
        System.out.println("======== successWithRetry3 end  测试动态代理失败重试三次=========");
        System.out.println();
    }

    /**
     * 测试saga事务
     *
     * @author giant
     * @date 2021年12月17日
     */
    public void methodA() {


        System.out.println("**********Gaga 测试 真正开始***********");
        Saga.begin();//saga开始。
        ServiceA serviceA = new ServiceA();
//               每个saga事务节点有两个实现，
//               如下:1、业务实现comfirm方法：sagaTest.test()方法；2、cancel方法为lambda方法实现
        Saga.service(serviceA, new ICallback<Throwable>() {
            @Override
            public void run(Throwable obj) {
                System.out.println("serviceA callback");
            }
        }).submit();

        ServiceB serviceB = new ServiceB();
        Saga.service(serviceB, new ICallback<Throwable>() {
            @Override
            public void run(Throwable obj) {
                System.out.println("serviceB callback");
            }
        }).submit();

        System.out.println("准备报错：");
        Saga.service(serviceA, (a) -> {
            System.out.println("回滚t3");
        }).fail();
//         sagaTest.fail()方法抛出异常，会执行Saga.rollback(throwable)方法，
        //        rollback方法会执行所有执行过commit方法的cancel方法，回滚所有执行过的comfirm；

        Saga.end();//结束本次分布式事务

//          test1.test2();
//          Test test2 = Fun.proxy(new Test(),null,null);
//          test2.test1();
//          test2.test2();
        //successWithRetry4重试四次失败抛出异常，不往下跑了
        System.out.println("不抛出异常跑完；");

    }


}

class ServiceA {
    public String submit() {
        System.out.println("test");
        return "test success";
    }

    public String fail() {
        System.out.println("fail:");
        throw new RuntimeException("fail");
    }

    public String fail(String msg) {
        System.out.println("fail:" + msg);
        throw new RuntimeException("fail");
    }
}

class ServiceB {
    public String submit() {
        System.out.println("test");
        return "test success";
    }

    public String fail() {
        System.out.println("fail:");
        throw new RuntimeException("fail");
    }

    public String fail(String msg) {
        System.out.println("fail:" + msg);
        throw new RuntimeException("fail");
    }
}


class Callback {

    public Callback() {
    }

    public static void success(Object obj) {
        System.out.println(obj + "成功");
    }

    public void fail(Throwable e) {
        System.out.println("执行失败");
    }
}
