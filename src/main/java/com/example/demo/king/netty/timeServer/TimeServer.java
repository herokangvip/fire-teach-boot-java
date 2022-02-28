package com.example.demo.king.netty.timeServer;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.CharsetUtil;

import java.net.InetSocketAddress;
import java.util.concurrent.ConcurrentHashMap;

public class TimeServer {
    private int port;

    public TimeServer(int port) {
        this.port = port;
    }

    private void run() {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {

                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            //outboundhandler一定要放在最后一个inboundhandler之前
                            //否则outboundhandler将不会执行到
                            socketChannel.pipeline().addLast(new EchoOutboundHandler3());
                            socketChannel.pipeline().addLast(new EchoOutboundHandler2());
                            socketChannel.pipeline().addLast(new EchoOutboundHandler1());

                            socketChannel.pipeline().addLast(new EchoInboundHandler1());
                            socketChannel.pipeline().addLast(new EchoInboundHandler2());
                            socketChannel.pipeline().addLast(new EchoInboundHandler3());

                            InetSocketAddress inetSocketAddress = socketChannel.remoteAddress();


                            System.out.println("========发送数据");
                            byte[] req = "测试数据".getBytes();
                            ByteBuf firstMessage = Unpooled.buffer(req.length);
                            firstMessage.writeBytes(req);
                            socketChannel.writeAndFlush(firstMessage);
                        }

                    })
                    .option(ChannelOption.SO_BACKLOG, 10000)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);
            System.out.println("TimeServer正在启动.");

            ChannelFuture channelFuture = serverBootstrap.bind(port).sync();
            System.out.println("TimeServer绑定端口" + port);

            /*for (int i = 0; i < 100; i++) {
                Thread.sleep(10000L);
                Channel channel = map.get("12345");
                if (channel == null) {
                    System.out.println("null");
                    continue;
                }
                byte[] req = ("服务端定期向客户端发送数据:" + i).getBytes();
                ByteBuf firstMessage = Unpooled.buffer(req.length);
                firstMessage.writeBytes(req);
                channel.writeAndFlush(firstMessage);
            }*/

            channelFuture.channel().closeFuture().sync();
            System.out.println("TimeServer已关闭.");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) {
        int port = 8080;
        if (args != null && args.length > 0) {
            try {
                port = Integer.parseInt(args[0]);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        TimeServer server = new TimeServer(port);
        server.run();
    }

    private static ConcurrentHashMap<String, Channel> map = new ConcurrentHashMap<>();

    private static class EchoInboundHandler1 extends ChannelInboundHandlerAdapter {
        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            System.out.println("进入 EchoInboundHandler1.channelRead");

            String data = ((ByteBuf) msg).toString(CharsetUtil.UTF_8);
            if (data.startsWith("userId")) {
                String userId = data.split("=")[1];
                Channel channel = ctx.channel();
                map.put(userId, channel);
            }
            System.out.println("EchoInboundHandler1.channelRead 收到数据：" + data);
            ctx.fireChannelRead(Unpooled.copiedBuffer("[EchoInboundHandler1] " + data, CharsetUtil.UTF_8));

            System.out.println("退出 EchoInboundHandler1 channelRead");
        }

        @Override
        public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
            System.out.println("[EchoInboundHandler1.channelReadComplete]");
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            System.out.println("[EchoInboundHandler1.exceptionCaught]" + cause.toString());
        }
    }

    private static class EchoInboundHandler2 extends ChannelInboundHandlerAdapter {
        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            System.out.println("进入 EchoInboundHandler2.channelRead");

            String data = ((ByteBuf) msg).toString(CharsetUtil.UTF_8);
            System.out.println("EchoInboundHandler2.channelRead 收到数据：" + data);
            ctx.fireChannelRead(Unpooled.copiedBuffer("[EchoInboundHandler2] " + data, CharsetUtil.UTF_8));

            System.out.println("退出 EchoInboundHandler2 channelRead");
        }

        @Override
        public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
            System.out.println("[EchoInboundHandler2.channelReadComplete]");
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            System.out.println("[EchoInboundHandler2.exceptionCaught]" + cause.toString());
        }
    }

    private static class EchoInboundHandler3 extends ChannelInboundHandlerAdapter {
        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            System.out.println("进入 EchoInboundHandler3.channelRead");

            String data = ((ByteBuf) msg).toString(CharsetUtil.UTF_8);
            System.out.println("EchoInboundHandler3.channelRead 收到数据：" + data);

            System.out.println("退出 EchoInboundHandler3 channelRead");
        }

        @Override
        public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
            System.out.println("[EchoInboundHandler3.channelReadComplete]");
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            System.out.println("[EchoInboundHandler3.exceptionCaught]" + cause.toString());
        }
    }

    private static class EchoOutboundHandler1 extends ChannelOutboundHandlerAdapter {
        @Override
        public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
            System.out.println("进入 EchoOutboundHandler1.write");

            //ctx.writeAndFlush(Unpooled.copiedBuffer("[第一次write中的write]", CharsetUtil.UTF_8));
            ctx.write(msg);

            System.out.println("退出 EchoOutboundHandler1.write");
        }
    }

    private static class EchoOutboundHandler2 extends ChannelOutboundHandlerAdapter {
        @Override
        public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
            System.out.println("进入 EchoOutboundHandler2.write");

            //ctx.writeAndFlush(Unpooled.copiedBuffer("[第一次write中的write]", CharsetUtil.UTF_8));
            ctx.write(msg);


            System.out.println("退出 EchoOutboundHandler2.write");
        }
    }

    private static class EchoOutboundHandler3 extends ChannelOutboundHandlerAdapter {
        @Override
        public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
            System.out.println("进入 EchoOutboundHandler3.write");

            //ctx.writeAndFlush(Unpooled.copiedBuffer("[第一次write中的write]", CharsetUtil.UTF_8));
            /*ctx.channel().writeAndFlush(Unpooled.copiedBuffer("在OutboundHandler里测试一下channel().writeAndFlush", CharsetUtil.UTF_8));
            ctx.write(msg);*/
            ctx.write(msg);

            System.out.println("退出 EchoOutboundHandler3.write");
        }
    }
}
