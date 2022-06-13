package com.example.demo.king.netty.hello;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.buffer.UnpooledByteBufAllocator;

/**
 * server
 *
 * @author sandykang
 */
public class MyByteBufTest {
    public static void main(String[] args) {
        //以系统默认为准建池化或非池化
        ByteBuf buffer3 = ByteBufAllocator.DEFAULT.buffer();
        ByteBuf buffer4 = ByteBufAllocator.DEFAULT.heapBuffer();

        //直接指定创建池化或非池化
        ByteBuf buffer5 = PooledByteBufAllocator.DEFAULT.buffer();
        ByteBuf buffer6 = UnpooledByteBufAllocator.DEFAULT.buffer();
    }
}
