package com.example.demo;

import javax.annotation.PostConstruct;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Test {
    private static ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

    /**
     * init
     */
    public static void main(String[] args) throws InterruptedException {

        executorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                triggerExecute();
            }
        }, 3L, 3L, TimeUnit.SECONDS);
        Thread.sleep(Integer.MAX_VALUE);
    }


    public static void triggerExecute()  {
        System.out.println("================");
        try {
            Thread.sleep(10000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
