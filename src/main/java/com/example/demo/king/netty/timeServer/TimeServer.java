package com.example.demo.king.netty.timeServer;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

public class TimeServer {

    public static void main(String[] args) {
        new TimeServer().bind(8089);
    }

    public void bind(int port) {
        //配置服务端的NIO线程组
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 1024)
//                    .childOption(ChannelOption.SO_KEEPALIVE, true)//维持链接的活跃，清除死链接
//                    .childOption(ChannelOption.TCP_NODELAY, true)//关闭延迟发送
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ByteBuf buf = Unpooled.copiedBuffer("#".getBytes());
                            socketChannel.pipeline()
                                    //.addLast(new LineBasedFrameDecoder(1024))
                                    .addLast(new DelimiterBasedFrameDecoder(1024, buf))
                                    .addLast(new StringDecoder())
                                    .addLast(new TimeServerHandler());
                        }
                    });
            //绑定端口同步等待成功
            ChannelFuture channelFuture = b.bind(port).sync();
            //等待服务端监听端口关闭
            channelFuture.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

}
