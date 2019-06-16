package com.example.demo.king.netty.echo;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import org.msgpack.MessagePack;
import org.msgpack.type.Value;

import java.util.List;

public class MsgpackDecoder extends MessageToMessageDecoder<ByteBuf> {
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        int length = byteBuf.readableBytes();
        byte[] arr = new byte[length];
        byteBuf.getBytes(byteBuf.readerIndex(), arr, 0, length);
        MessagePack messagePack = new MessagePack();
        Value read = messagePack.read(arr);
        list.add(read);
    }
}
