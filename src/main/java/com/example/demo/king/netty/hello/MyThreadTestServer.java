package com.example.demo.king.netty.hello;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

/**
 * server
 *
 * @author sandykang
 */
public class MyThreadTestServer {
    public static void main(String[] args) throws Exception {

        //selector线程组
        NioEventLoopGroup boos = new NioEventLoopGroup(2);
        //worker线程组
        NioEventLoopGroup worker = new NioEventLoopGroup(2);
        ServerBootstrap bootstrap = new ServerBootstrap();
        ChannelFuture channelFuture = bootstrap.group(boos, worker).channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel nioSocketChannel) throws Exception {
                        nioSocketChannel.pipeline()
                                //.addLast(new LoggingHandler(LogLevel.DEBUG))
                                //out，执行顺序：↑
                                .addLast(new StringEncoder())
                                //in ，执行顺序：↓
                                .addLast(new StringDecoder())
                                .addLast(new ChannelInboundHandlerAdapter() {
                                    @Override
                                    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                        System.out.println(Thread.currentThread().getName() + "服务端收到数据：" + msg);
                                        ctx.channel().writeAndFlush("(ack)server has received data：" + msg);
                                        ctx.fireChannelRead(msg);
                                    }
                                });
                    }
                })
                .bind(8080)
                .sync();

        channelFuture.channel().closeFuture().sync();
    }
}
