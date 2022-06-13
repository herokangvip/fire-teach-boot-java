package com.example.demo.king.netty.hello;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.net.InetSocketAddress;
import java.util.Scanner;

/**
 * server
 *
 * @author sandykang
 */
public class HelloClient {
    public static void main(String[] args) throws Exception {
        NioEventLoopGroup worker = new NioEventLoopGroup(2);
        //耗时任务，每个handler可以指定其处理任务的executor，如不指定使用的还是EventLoop绑定的线程执行
        NioEventLoopGroup longTimeWorker = new NioEventLoopGroup(2);
        Channel channel = new Bootstrap()
                .group(worker)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        System.out.println("客户端已连接到服务器");
                        ch.pipeline()
                                //out，执行顺序：↑
                                .addLast(new StringEncoder())
                                //in ，执行顺序：↓
                                .addLast(new StringDecoder())
                                .addLast(longTimeWorker, "longTimeWorker", new ChannelInboundHandlerAdapter() {
                                    @Override
                                    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                        System.out.println(msg);
                                        super.channelRead(ctx, msg);
                                    }
                                });
                    }
                })
                .connect(new InetSocketAddress("127.0.0.1", 8080))
                .sync().channel();
        channel.writeAndFlush("ping");


        Channel channel2 = new Bootstrap()
                .group(worker)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        System.out.println("客户端已连接到服务器");
                        ch.pipeline()
                                //out，执行顺序：↑
                                .addLast(new StringEncoder())
                                //in ，执行顺序：↓
                                .addLast(new StringDecoder())
                                .addLast(longTimeWorker, "longTimeWorker", new ChannelInboundHandlerAdapter() {
                                    @Override
                                    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                        System.out.println(msg);
                                        super.channelRead(ctx, msg);
                                    }
                                });
                    }
                })
                .connect(new InetSocketAddress("127.0.0.1", 8081))
                .sync().channel();

        new Thread(() -> {
            Scanner scanner = new Scanner(System.in);
            while (true) {
                String str = scanner.nextLine();
                if ("q".equals(str)) {
                    channel.close();
                    //优雅关闭
                    worker.shutdownGracefully();
                    longTimeWorker.shutdownGracefully();
                    break;
                }
                channel.writeAndFlush(str);
            }
        }).start();
        //监听关闭事件
        channel.closeFuture().addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture channelFuture) throws Exception {
                System.out.println("客户端主动关闭连接");
            }
        });
    }
}
