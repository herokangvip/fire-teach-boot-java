package com.example.demo.king.netty.msgpackEcho;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.LineBasedFrameDecoder;

import java.util.List;

public class EchoClient {
    public static void main(String[] args) {
        new EchoClient().connect("127.0.0.1", 8080);
    }

    void connect(String host, int port) {
        NioEventLoopGroup group = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        try {
            bootstrap.group(group).channel(NioSocketChannel.class)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .option(ChannelOption.TCP_NODELAY, true)//关闭延迟发送
                    .handler(new ChannelInitializer<SocketChannel>() {

                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(new LineBasedFrameDecoder(1024))
                                    .addLast(new LengthFieldBasedFrameDecoder(1024,0,2,0,2))
                                    .addLast(new MsgpackDecoder())
                                    .addLast(new LengthFieldPrepender(2))
                                    .addLast(new MsgpackEncoder())
                                    .addLast(new ChannelInboundHandlerAdapter() {
                                        @Override
                                        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                            //String s = (String) msg;
                                            //System.out.println("收到服务端消息:" + msg);
                                        }

                                        @Override
                                        public void channelActive(ChannelHandlerContext ctx) throws Exception {
                                            /*for (int i = 0; i < 10; i++) {
                                                byte[] data = "123456789\r\n".getBytes();
                                                ByteBuf buf = Unpooled.copiedBuffer(data);
                                                ctx.writeAndFlush(buf);
                                            }*/
                                            List<User> users = User.getUsers();
                                            for (User user:users) {
                                                ctx.write(user);
                                            }
                                            ctx.flush();
                                        }
                                    });
                        }
                    });
            ChannelFuture channelFuture = bootstrap.connect(host, port).sync();
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully();
        }
    }
}
