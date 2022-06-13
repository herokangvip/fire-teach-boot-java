package com.example.demo.king.netty.hello;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.concurrent.DefaultEventExecutor;
import io.netty.util.concurrent.DefaultEventExecutorGroup;

import java.util.List;

/**
 * server
 *
 * @author sandykang
 */
public class HelloServer {
    public static void main(String[] args) throws Exception {
        //selector线程组
        NioEventLoopGroup boos = new NioEventLoopGroup(2);
        System.out.println("boos:" + boos);
        //worker线程组
        NioEventLoopGroup worker = new NioEventLoopGroup(2);
        System.out.println("worker:" + worker);

        ChannelFuture future2 = new ServerBootstrap()
                .group(boos, worker)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        ch.pipeline()
                                .addLast(new StringDecoder())
                                .addLast(new ChannelDuplexHandler() {
                                    @Override
                                    public void channelActive(ChannelHandlerContext ctx) throws Exception {
                                        System.out.println(Thread.currentThread().getName() + "channelActive11111");
                                        super.channelActive(ctx);
                                    }

                                    @Override
                                    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                        System.out.println(Thread.currentThread().getName() + "服务端2收到数据：" + msg);
                                        super.channelRead(ctx, msg);
                                    }
                                });
                    }
                })
                .bind(8081)
                .sync();

        DefaultEventExecutor eventExecutor = new DefaultEventExecutor();
        DefaultEventExecutorGroup eventExecutors = new DefaultEventExecutorGroup(2);
        try {
            ChannelFuture future = new ServerBootstrap()
                    .group(boos, worker)
                    .channel(NioServerSocketChannel.class)
                    //连接队列大小，默认128取机器配置和程序配置中小的一个，如果满了会拒绝连接
                    .option(ChannelOption.SO_BACKLOG, 128)
                    //默认是false开启了nagle算法，一般高性能服务器会设置为true
                    .option(ChannelOption.TCP_NODELAY, true)
                    .childHandler(new ChannelInitializer<NioSocketChannel>() {
                        @Override
                        protected void initChannel(NioSocketChannel nioSocketChannel) throws Exception {
                            nioSocketChannel.pipeline()
                                    .addLast(new ChannelDuplexHandler() {
                                        @Override
                                        public void channelActive(ChannelHandlerContext ctx) throws Exception {
                                            System.out.println(Thread.currentThread().getName() + "channelActive22222");
                                            super.channelActive(ctx);
                                        }
                                    })
                                    //.addLast(new LoggingHandler(LogLevel.DEBUG))
                                    //out，执行顺序：↑
                                    .addLast(new StringEncoder() {
                                        @Override
                                        protected void encode(ChannelHandlerContext ctx, CharSequence msg, List<Object> out) throws Exception {
                                            System.out.println(Thread.currentThread().getName() + "encode");
                                            super.encode(ctx, msg, out);
                                        }
                                    })
                                    //in ，执行顺序：↓
                                    .addLast(new StringDecoder() {
                                        @Override
                                        protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
                                            System.out.println(Thread.currentThread().getName() + "decode");
                                            super.decode(ctx, msg, out);
                                        }
                                    })
                                    //.addLast(new LengthFieldBasedFrameDecoder(0,2,
                                    //        0,2,2))
                                    .addLast("test", new ChannelInboundHandlerAdapter() {
                                        @Override
                                        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                            ByteBuf buffer = ctx.alloc().buffer();
                                            System.out.println(Thread.currentThread().getName() + "服务端收到数据：" + msg);
                                            ctx.channel().writeAndFlush("(ack)server has received data：" + msg);
                                            ctx.fireChannelRead(msg);
                                            ctx.executor().execute(new Runnable() {
                                                @Override
                                                public void run() {
                                                    System.out.println(Thread.currentThread().getName()
                                                            + "服务端收到数据后执行业务线程");
                                                }
                                            });
                                        }
                                    });
                        }
                    })
                    .bind(8080)
                    .sync();
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            boos.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }
}
