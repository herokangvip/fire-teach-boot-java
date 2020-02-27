package com.example.demo.king.thread;

import com.example.demo.king.singleton.Connection;

import java.util.concurrent.locks.ReentrantLock;

public class ThreadTest {
    private static ReentrantLock lock = new ReentrantLock();
    static class ThreadC extends Thread{
        @Override
        public void run() {
            try{
                lock.lock();
                for (int i = 0; i <1000000 ; i++) {
                    if(Thread.currentThread().isInterrupted()){
                        throw new InterruptedException();
                    }
                    System.out.println(i);
                }
            }catch (InterruptedException e){
                e.printStackTrace();
            }finally {
                lock.unlock();
            }

        }
    }

    public static void main(String[] args) throws Exception{
        Connection connection = ConnectionEnum.instance.getConnection();
    }
}
