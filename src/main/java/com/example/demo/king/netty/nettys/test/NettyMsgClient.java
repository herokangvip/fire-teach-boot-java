package com.example.demo.king.netty.nettys.test;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class NettyMsgClient {
    public static ChannelFuture future = null;

    public static ScheduledExecutorService executor =  Executors.newSingleThreadScheduledExecutor();

    public void connect(String ip, int port) {
        NioEventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group).channel(NioSocketChannel.class)
                    .option(ChannelOption.SO_KEEPALIVE, true)//长连接
                    .option(ChannelOption.TCP_NODELAY, true)//关闭延迟发送
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ByteBuf buf = Unpooled.copiedBuffer("#".getBytes());
                            socketChannel.pipeline()
                                    .addLast(new IdleStateHandler(2,0,0))
                                    .addLast(new DelimiterBasedFrameDecoder(1024, buf))
                                    .addLast(new StringDecoder())
                                    .addLast(new HandClientHandler());
                        }
                    });
            //发起异步连接操作
            ChannelFuture sync = bootstrap.connect(ip, port).sync();
            future = sync;
            //等待客户端链路关闭
            sync.channel().closeFuture().sync();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully();
            executor.scheduleWithFixedDelay(new Runnable() {
                @Override
                public void run() {
                    new NettyMsgClient().connect("127.0.0.1", 8089);
                }
            },0, 5 , TimeUnit.SECONDS);
        }
    }

    public static void main(String[] args) throws Exception {
        new Thread(() -> new NettyMsgClient().connect("127.0.0.1", 8089)).start();
    }

}
