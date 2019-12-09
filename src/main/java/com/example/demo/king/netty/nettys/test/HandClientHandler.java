package com.example.demo.king.netty.nettys.test;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

public class HandClientHandler extends ChannelInboundHandlerAdapter {


    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        byte[] req = "handClient#".getBytes();
        ByteBuf firstMessage = Unpooled.buffer(req.length);
        firstMessage.writeBytes(req);
        ctx.writeAndFlush(firstMessage);
    }

    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("客户端收到握手消息" + msg.toString());
        String body = (String) msg;
        if ("handServer".equals(body)) {
            ctx.fireChannelRead(msg);
        }
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent idleStateEvent = (IdleStateEvent)evt;
            if(idleStateEvent.state()== IdleState.ALL_IDLE){
                byte[] req = "客户端心跳消息#".getBytes();
                ByteBuf firstMessage = Unpooled.buffer(req.length);
                firstMessage.writeBytes(req);
                ctx.writeAndFlush(firstMessage);
            }
            System.out.println("===============");
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("客户端连接异常");
        //cause.printStackTrace();
        ctx.close();
    }
}
