package com.example.demo.king.netty.nettys.test;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.text.SimpleDateFormat;
import java.util.Date;

public class HandServerHandler extends ChannelInboundHandlerAdapter {


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String body = (String) msg;
        if ("handClient".equals(body)) {
            System.out.println("服务端收到握手消息"+msg.toString());
            ByteBuf resp = Unpooled.copiedBuffer("handServer#".getBytes());
            ctx.writeAndFlush(resp);
        }
        if("heartClient".equals(body)){
            ctx.fireChannelRead(msg);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
