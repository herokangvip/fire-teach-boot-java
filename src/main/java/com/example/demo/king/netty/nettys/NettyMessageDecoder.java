package com.example.demo.king.netty.nettys;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import org.msgpack.MessagePack;

/**
 * @author k
 * @version 1.0
 * @date 2019/11/24 21:56
 */
public class NettyMessageDecoder extends LengthFieldBasedFrameDecoder {

    public NettyMessageDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength, int lengthAdjustment, int initialBytesToStrip) {
        super(maxFrameLength, lengthFieldOffset, lengthFieldLength, lengthAdjustment, initialBytesToStrip);
    }

    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        ByteBuf decode = (ByteBuf) super.decode(ctx, in);
        if (decode == null) {
            return null;
        }
        int length = decode.readableBytes();
        byte[] arr = new byte[length];
        decode.getBytes(decode.readerIndex(), arr, 0, length);
        MessagePack messagePack = new MessagePack();
        NettyMessage read = messagePack.read(arr, NettyMessage.class);
        return read;
    }


}
