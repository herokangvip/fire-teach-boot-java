package com.example.demo.king.netty.nettys;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.msgpack.MessagePack;

import java.util.HashMap;

/**
 * @author k
 * @version 1.0
 * @date 2019/11/25 15:22
 */
public class NettyMessageEncoder extends MessageToByteEncoder<NettyMessage> {
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, NettyMessage nettyMessage, ByteBuf byteBuf) throws Exception {
        MessagePack messagePack = new MessagePack();
        byte[] write = messagePack.write(nettyMessage);
        byteBuf.writeBytes(write);
    }

    public static void main(String[] args) throws Exception{
        NettyMessage message = new NettyMessage();
        message.setLength(100);
        message.setType(new Byte("1"));
        HashMap<String, String> map = new HashMap<>();
        message.setBody(map);
        MessagePack messagePack = new MessagePack();
        byte[] write = messagePack.write(message);
        String s = conver2HexStr(write);
        System.out.println(s);

        long a = 9382758923365L;
        System.out.println(a/1000);
    }




    /**
     * byte数组转换为二进制字符串,每个字节以","隔开
     **/
    public static String conver2HexStr(byte[] b) {
        StringBuffer result = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            result.append(Long.toString(b[i] & 0xff, 2) + ",");
        }
        return result.toString().substring(0, result.length() - 1);
    }

    /**
     * 二进制字符串转换为byte数组,每个字节以","隔开
     **/
    public static byte[] conver2HexToByte(String hex2Str) {
        String[] temp = hex2Str.split(",");
        byte[] b = new byte[temp.length];
        for (int i = 0; i < b.length; i++) {
            b[i] = Long.valueOf(temp[i], 2).byteValue();
        }
        return b;
    }

}
