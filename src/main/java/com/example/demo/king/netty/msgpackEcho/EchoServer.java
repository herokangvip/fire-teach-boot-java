package com.example.demo.king.netty.msgpackEcho;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;

public class EchoServer {
    public static void main(String[] args) {
        new EchoServer().bind(8080);
    }

    void bind(int port) {
        NioEventLoopGroup boosGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        try {
            serverBootstrap.group(boosGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline()
                                    //.addLast(new LineBasedFrameDecoder(1024))
                                    //.addLast(new StringDecoder())
                                    .addLast(new LengthFieldBasedFrameDecoder(1024,0,2,0,2))
                                    .addLast(new MsgpackDecoder())
                                    .addLast(new LengthFieldPrepender(2))
                                    .addLast(new MsgpackEncoder())
                                    .addLast(new ChannelInboundHandlerAdapter() {
                                        @Override
                                        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                            //String response = (String) msg;
                                            //User user = (User)msg;
                                            System.out.println("从客户端收到消息:" + msg);
                                            //String data = response + "\r\n";
                                            //ctx.writeAndFlush(Unpooled.copiedBuffer(data.getBytes()));
                                            //ctx.writeAndFlush(Unpooled.copiedBuffer(user.toString().getBytes()));
                                        }
                                    });
                        }
                    });
            ChannelFuture channelFuture = serverBootstrap.bind(port).sync();
            channelFuture.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            boosGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }

    }
}
