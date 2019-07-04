package com.example.demo.king.netty.msgpackEcho;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.msgpack.MessagePack;
import org.msgpack.type.Value;

import java.util.List;

public class MsgpackDecoder extends ByteToMessageDecoder {
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
