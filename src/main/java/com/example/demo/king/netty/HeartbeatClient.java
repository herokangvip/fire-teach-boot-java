package com.example.demo.king.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.SimpleUserEventChannelHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.PostConstruct;

/**
 * server
 *
 * @author sandykang
 */
//@Component
public class HeartbeatClient {
    private final static Logger LOGGER = LoggerFactory.getLogger(HeartbeatClient.class);

    private EventLoopGroup group = new NioEventLoopGroup();

    @Value("${netty.server.host:127.0.0.1}")
    private String host;

    @Value("${netty.server.port:8002}")
    private int nettyPort;


    private SocketChannel channel;

    @PostConstruct
    public void start() throws InterruptedException {
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .handler(new CustomerHandleInitializer())
        ;

        ChannelFuture future = bootstrap.connect(host, nettyPort).sync();

        if (future.isSuccess()) {
            LOGGER.info("启动 Netty 成功");
        }
        channel = (SocketChannel) future.channel();
    }

    private static class CustomerHandleInitializer extends ChannelInitializer<NioSocketChannel> {
        @Override
        protected void initChannel(NioSocketChannel ch) throws Exception {
            ch.pipeline()
                    //10 秒没发送消息 将IdleStateHandler 添加到 ChannelPipeline 中
                    .addLast(new IdleStateHandler(0, 10, 0))
                    .addLast(new HeartbeatEncode())
                    //.addLast(new EchoClientHandle())
                    .addLast(new SimpleUserEventChannelHandler<IdleStateEvent>() {
                        @Override
                        protected void eventReceived(ChannelHandlerContext ctx,
                                                     IdleStateEvent idleStateEvent) throws Exception {
                            if (idleStateEvent.state() == IdleState.WRITER_IDLE) {
                                LOGGER.info("已经 10 秒没有发送信息！");
                                //向服务端发送消息
                                ctx.alloc().buffer();
                                //CustomProtocol heartBeat = ApplicationContextUtil.getBean(CustomProtocol.class);
                                CustomProtocol heartBeat = new CustomProtocol(1L, "ping");
                                ctx.writeAndFlush(heartBeat).addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
                            } else {
                                ctx.fireUserEventTriggered(idleStateEvent);
                            }
                        }
                    })
                    .addLast(new SimpleChannelInboundHandler<ByteBuf>() {
                        @Override
                        protected void channelRead0(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf) throws Exception {
                            //从服务端收到消息时被调用
                            LOGGER.info("客户端收到消息={}", byteBuf.toString(CharsetUtil.UTF_8));
                        }
                    })
            ;
        }
    }
}
