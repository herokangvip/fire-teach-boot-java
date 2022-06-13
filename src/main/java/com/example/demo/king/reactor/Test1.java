package com.example.demo.king.reactor;

import reactor.core.publisher.Mono;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

/**
 * server
 *
 * @author sandykang
 */
public class Test1 {
    public static void main(String[] args) throws Exception {
        Mono<String> mono = Mono.fromCallable(new Callable<String>() {

            @Override
            public String call() throws Exception {
                return "133";
            }
        });
        CompletableFuture<String> completableFuture = new CompletableFuture<>();
        mono.doOnError(throwable -> System.out.println("error"))
                .doOnNext(completableFuture::complete)
                .subscribe(
                        s -> System.out.println("subscribe"),
                        completableFuture::completeExceptionally
                );

        completableFuture.whenComplete(new BiConsumer<String, Throwable>() {
            @Override
            public void accept(String s, Throwable throwable) {

            }
        });
        System.out.println(completableFuture.get());

    }
}
