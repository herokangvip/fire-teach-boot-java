package com.example.demo.king.netty.timeServer;

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
                            ByteBuf buf = Unpooled.copiedBuffer("#".getBytes());
                            socketChannel.pipeline()
                                    //.addLast(new LineBasedFrameDecoder(1024))
                                    .addLast(new DelimiterBasedFrameDecoder(1024, buf))
                                    .addLast(new StringDecoder())
                                    .addLast(new TimeClientHandler());
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
        }
    }

    public static void main(String[] args) throws Exception {
        new Thread(() -> new TimeClient().connect("127.0.0.1", 8089)).start();

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
