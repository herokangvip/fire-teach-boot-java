package com.example.demo.king.thread.pool;

import java.util.concurrent.*;

public class CountDownLatchTest {
    public static void main(String[] args) throws Exception {
        // 发音[ˈsaɪklɪk], [ˈbæriə(r)]
        CyclicBarrier cyclicBarrier = new CyclicBarrier(2, () -> {
            System.out.println(Thread.currentThread().getName() + "最后到的人罚酒三杯");
        });
        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    //Thread.sleep(1000L);
                    System.out.println("线程 1：我到了" + System.currentTimeMillis() / 1000);
                    cyclicBarrier.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }
                System.out.println("线程 1：我要开吃了" + System.currentTimeMillis() / 1000);
            }
        };
        thread.start();
        Thread thread2 = new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(5000L);
                    System.out.println("线程 2：我到了" + System.currentTimeMillis() / 1000);
                    cyclicBarrier.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }
                System.out.println("线程 2：我要开吃了" + System.currentTimeMillis() / 1000);
            }
        };
        thread2.start();

        Thread.sleep(1000L);
        thread.interrupt();

        Thread.sleep(6000L);
        //cyclicBarrier.reset();

        Thread thread3 = new Thread() {
            @Override
            public void run() {
                try {
                    //Thread.sleep(1000L);
                    System.out.println("线程 1：我到了" + System.currentTimeMillis() / 1000);
                    cyclicBarrier.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }
                System.out.println("线程 1：我要开吃了" + System.currentTimeMillis() / 1000);
            }
        };
        thread3.start();
        Thread thread4 = new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(5000L);
                    System.out.println("线程 2：我到了" + System.currentTimeMillis() / 1000);
                    cyclicBarrier.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }
                System.out.println("线程 2：我要开吃了" + System.currentTimeMillis() / 1000);
            }
        };
        thread4.start();

    }
}
