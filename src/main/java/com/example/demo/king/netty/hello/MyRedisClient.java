package com.example.demo.king.netty.hello;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.UnpooledByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;

import java.net.InetSocketAddress;

/**
 * server
 *
 * @author sandykang
 */
public class MyRedisClient {
    /**
     eg:get test,redis协议发送命令依次为：*命令数组长度\r\n  $数组中字符串长度\r\n  $数组中字符串长度\r\n
     *2
     $3
     get
     $4
     test
     */
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
                                .addLast(new StringDecoder())
                                .addLast(new ChannelInboundHandlerAdapter() {
                                    @Override
                                    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                        System.out.println("收到redis返回：" + msg);
                                        super.channelRead(ctx, msg);
                                    }
                                });
                    }
                })
                .connect(new InetSocketAddress("127.0.0.1", 6379))
                .sync().channel();


        //*2
        //     $3
        //     get
        //     $4
        //     test
        ByteBuf buffer = UnpooledByteBufAllocator.DEFAULT.buffer();
        buffer.writeBytes("*2\r\n".getBytes());
        buffer.writeBytes("$3\r\n".getBytes());
        buffer.writeBytes("get\r\n".getBytes());
        buffer.writeBytes("$4\r\n".getBytes());
        buffer.writeBytes("test\r\n".getBytes());
        channel.writeAndFlush(buffer);

    }
}
