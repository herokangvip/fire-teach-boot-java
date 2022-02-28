package com.example.demo.king.netty.timeServer;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.CharsetUtil;

public class TimeClient {
    public static ChannelFuture future = null;

    public void connect(String ip, int port) {
        NioEventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group).channel(NioSocketChannel.class)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .option(ChannelOption.TCP_NODELAY, true)//关闭延迟发送
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            //ByteBuf buf = Unpooled.copiedBuffer("#".getBytes());
                            socketChannel.pipeline()
                                    .addLast(new ChannelInitializer<SocketChannel>() {

                                        @Override
                                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                                            System.out.println("initChannel");
                                            socketChannel.pipeline().addLast(new ChannelDuplexHandler() {
                                                @Override
                                                public void channelActive(ChannelHandlerContext ctx) throws Exception {
                                                    System.out.println("========：channelActive");
                                                    ctx.fireChannelActive();
                                                    /*System.out.println("=====channel：初始化");
                                                    byte[] req = "userId=12345".getBytes();
                                                    ByteBuf firstMessage = Unpooled.buffer(req.length);
                                                    firstMessage.writeBytes(req);
                                                    ctx.writeAndFlush(firstMessage);*/
                                                }


                                                @Override
                                                public void channelInactive(ChannelHandlerContext ctx) throws Exception {
                                                    System.out.println("========：channelInactive");
                                                    super.channelInactive(ctx);
                                                }

                                                @Override
                                                public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                                    System.out.println("========：channelRead");
                                                    String data = ((ByteBuf) msg).toString(CharsetUtil.UTF_8);
                                                    System.out.println("client端收到数据====:" + data);
                                                    super.channelRead(ctx, msg);
                                                }

                                                @Override
                                                public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
                                                    System.out.println("========：channelReadComplete");
                                                    super.channelReadComplete(ctx);
                                                }
                                            });
                                        }


                                    });
                                    /*.addLast(new ChannelInboundHandlerAdapter() {
                                        @Override
                                        public void channelActive(ChannelHandlerContext ctx) throws Exception {
                                            ctx.fireChannelActive();
                                            System.out.println("=====channel：初始化");
                                            byte[] req = "userId=12345".getBytes();
                                            ByteBuf firstMessage = Unpooled.buffer(req.length);
                                            firstMessage.writeBytes(req);
                                            ctx.writeAndFlush(firstMessage);
                                        }

                                        @Override
                                        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                            String data = ((ByteBuf) msg).toString(CharsetUtil.UTF_8);
                                            System.out.println("client端收到数据====:" + data);

                                            *//*System.out.println("=====channel：发送数据");
                                            byte[] req = "userId=12345".getBytes();
                                            ByteBuf firstMessage = Unpooled.buffer(req.length);
                                            firstMessage.writeBytes(req);
                                            ctx.writeAndFlush(firstMessage);*//*
                                        }
                                    });*/
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
            //group.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception {
        new Thread(
                () -> new TimeClient().connect("127.0.0.1", 8080)
        ).start();

        //控制台输入程序
        /*Thread.sleep(1000L);
        System.out.println("请输入一个字符");
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()){
            System.out.println("请输入一个字符");
            String str = scanner.next();
            System.out.println(str);
            byte[] req = str.getBytes();
            ByteBuf message = Unpooled.buffer(req.length);
            message.writeBytes(req);
            future.channel().writeAndFlush(message);
        }*/


    }

}
