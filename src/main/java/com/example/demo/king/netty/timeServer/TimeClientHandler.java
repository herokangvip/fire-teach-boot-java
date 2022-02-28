package com.example.demo.king.netty.timeServer;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.nio.charset.StandardCharsets;

public class TimeClientHandler extends ChannelInboundHandlerAdapter {

    private int i = 0;
    private StringBuilder sb = new StringBuilder();

    public TimeClientHandler() {


    }

    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        for (int i = 0; i < 1; i++) {
            byte[] req = "测试数据".getBytes();
            ByteBuf firstMessage = Unpooled.buffer(req.length);
            firstMessage.writeBytes(req);
            ctx.writeAndFlush(firstMessage);
        }
    }

    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        i++;
        /*ByteBuf buf = (ByteBuf) msg;
        byte[] req = new byte[buf.readableBytes()];
        buf.readBytes(req);
        String body = new String(req, "UTF-8");*/
        String body = (String) msg;
        sb.append(body);
        System.out.println("now is :" + body);
        System.out.println("=============" + i);
        //System.out.println("=============" + sb.toString());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
