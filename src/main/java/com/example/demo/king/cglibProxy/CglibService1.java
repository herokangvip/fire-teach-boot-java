package com.example.demo.king.cglibProxy;

/**
 * @author k
 * @version 1.0
 * @date 2020/1/11 17:59
 */
public class CglibService1 implements CglibInterface {
    @Override
    public void put() {
        try {
            Thread.sleep(100L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("put01");
    }

    @Override
    public void del() {
        try {
            Thread.sleep(100L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("del01");
    }
}
