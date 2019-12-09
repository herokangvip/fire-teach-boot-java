package com.example.demo.king.thread;

public class WaitNotify {
    private static Object obj = new Object();

    public static void main(String[] args) throws Exception {
        ThreadA threadA = new ThreadA();
        threadA.start();
        Thread.sleep(1000L);
        synchronized (obj) {
            Thread.sleep(20000L);
        }
    }

    static class ThreadA extends Thread {
        @Override
        public void run() {
            try {
                synchronized (obj) {
                    System.out.println("wait start");
                    obj.wait(3000L);
                    System.out.println("wait end");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
