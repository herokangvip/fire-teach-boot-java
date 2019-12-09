package com.example.demo.king.netty.nettys.test;

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
import io.netty.handler.timeout.IdleStateHandler;

public class NettyMsgServer {

    public static void main(String[] args) {
        new NettyMsgServer().bind(8089);
    }

    public void bind(int port) {
        //配置服务端的NIO线程组
        NioEventLoopGroup selectorGroup = new NioEventLoopGroup();
        NioEventLoopGroup ioGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(selectorGroup, ioGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)//维持链接的活跃，清除死链接
                    .childOption(ChannelOption.TCP_NODELAY, true)//关闭延迟发送
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ByteBuf buf = Unpooled.copiedBuffer("#".getBytes());
                            socketChannel.pipeline()
                                    .addLast(new IdleStateHandler(2,0,0))
                                    .addLast(new DelimiterBasedFrameDecoder(1024, buf))
                                    .addLast(new StringDecoder())
                                    .addLast(new HandServerHandler());
                        }
                    });
            //绑定端口同步等待成功
            ChannelFuture channelFuture = bootstrap.bind(port).sync();
            //等待服务端监听端口关闭
            channelFuture.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println("退出=======");
            selectorGroup.shutdownGracefully();
            ioGroup.shutdownGracefully();
        }
    }

}
