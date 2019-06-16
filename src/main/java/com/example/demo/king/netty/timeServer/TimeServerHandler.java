package com.example.demo.king.netty.timeServer;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeServerHandler extends ChannelInboundHandlerAdapter {
    private int i=0;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        i++;
        /*ByteBuf buf = (ByteBuf) msg;
        byte[] req = new byte[buf.readableBytes()];
        buf.readBytes(req);*/
        //String body = new String(req, "UTF-8");
        String body = (String)msg;
        System.out.println("The time server receive order:" + body);
        String currentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())+"#_";
        ByteBuf resp = Unpooled.copiedBuffer(currentTime.getBytes());
        ctx.write(resp);
        ctx.flush();
        System.out.println("=============="+i);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
