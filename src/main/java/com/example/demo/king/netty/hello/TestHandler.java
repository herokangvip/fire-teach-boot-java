package com.example.demo.king.netty.hello;

import com.king.demo.king.netty.CustomProtocol;
import io.netty.buffer.UnpooledByteBufAllocator;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.channel.SimpleUserEventChannelHandler;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;

/**
 * server
 *
 * @author sandykang
 */
public class TestHandler {
    public static void main(String[] args) throws InterruptedException {
        ChannelInboundHandlerAdapter in1 = new ChannelInboundHandlerAdapter() {
            @Override
            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                System.out.println("in:1");
                super.channelRead(ctx, msg);
            }
        };
        ChannelInboundHandlerAdapter in2 = new ChannelInboundHandlerAdapter() {
            @Override
            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                System.out.println("in:2");
                super.channelRead(ctx, msg);
                ctx.write("==");
            }
        };
        ChannelOutboundHandlerAdapter out1 = new ChannelOutboundHandlerAdapter() {
            @Override
            public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
                System.out.println("out:1");
                super.write(ctx, msg, promise);
            }
        };
        ChannelOutboundHandlerAdapter out2 = new ChannelOutboundHandlerAdapter() {
            @Override
            public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
                System.out.println("out:2");
                super.write(ctx, msg, promise);
                //不能用会死循环
                //ctx.channel().writeAndFlush("==");
            }
        };

        IdleStateHandler idleStateHandler = new IdleStateHandler(0,
                10, 0);


        SimpleUserEventChannelHandler<IdleStateEvent> idleReceive = new SimpleUserEventChannelHandler<IdleStateEvent>() {
            @Override
            protected void eventReceived(ChannelHandlerContext ctx,
                                         IdleStateEvent idleStateEvent) throws Exception {
                if (idleStateEvent.state() == IdleState.WRITER_IDLE) {
                    System.out.println("已经 10 秒没有发送信息！");
                    //向服务端发送消息
                    ctx.alloc().buffer();
                    //CustomProtocol heartBeat = ApplicationContextUtil.getBean(CustomProtocol.class);
                    CustomProtocol heartBeat = new CustomProtocol(1L, "ping");
                    ctx.writeAndFlush(heartBeat).addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
                }
                ctx.fireUserEventTriggered(idleStateEvent);
            }
        };

        StringDecoder stringDecoder = new StringDecoder();
        StringEncoder stringEncoder = new StringEncoder();

        EmbeddedChannel channel = new EmbeddedChannel(out1, out2, stringEncoder,
                stringDecoder, idleStateHandler, idleReceive, in1, in2);
        channel.writeInbound(UnpooledByteBufAllocator.DEFAULT.buffer().writeBytes("hello".getBytes()));
        System.out.println("==============");
        //channel.writeOutbound(UnpooledByteBufAllocator.DEFAULT.buffer().writeBytes("hello".getBytes()));
        //NioEventLoopGroup worker = new NioEventLoopGroup(2);
        channel.closeFuture().sync();

    }
}
