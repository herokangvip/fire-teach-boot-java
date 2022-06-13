package com.example.demo.king.netty.test;

import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;

/**
 * server
 *
 * @author sandykang
 */
public class MyClient {
    public static void main(String[] args) throws Exception {
        SocketChannel sc = SocketChannel.open();
        sc.connect(new InetSocketAddress("127.0.0.1", 8080));
        System.out.println("====");
        //sc.write(StandardCharsets.UTF_8.encode("hello"));
        Thread.sleep(1000L);
        System.exit(-1);
        //sc.write(StandardCharsets.UTF_8.encode("hello world"));
    }
}
