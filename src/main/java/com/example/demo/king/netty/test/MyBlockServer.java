package com.example.demo.king.netty.test;

import cn.hutool.core.io.BufferUtil;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * server
 *
 * @author sandykang
 */
public class MyBlockServer {

    public static void main(String[] args) throws Exception {
        //block();//阻塞
        //unBlock();//非阻塞，但是有轮训
        selector();//非阻塞 selector
    }

    private static void selector() throws Exception {
        //selector轮询器
        Selector selector = Selector.open();
        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.configureBlocking(false);
        //将Channel注册到selector轮询器,设置关注的事件（accept，connect,read,write）
        SelectionKey sscKey = ssc.register(selector, 0, null);
        sscKey.interestOps(SelectionKey.OP_ACCEPT);

        ssc.bind(new InetSocketAddress(8080));
        while (true) {
            //没有事件时阻塞，有事件时执行
            selector.select();
            //取出有事件的SelectionKey
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            while (iterator.hasNext()) {
                SelectionKey selectionKey = iterator.next();
                //selector的keys(所有注册的)和selectedKeys(所有注册的中有事件发生的)是两个不同的集合，
                // selectedKeys是一次selector.select()后有事件的集合，不会自己移除，需要使用方处理完相应的事件后自己移除
                iterator.remove();
                //连接事件
                if (selectionKey.isAcceptable()) {
                    ServerSocketChannel channel = (ServerSocketChannel) selectionKey.channel();
                    SocketChannel socketChannel = channel.accept();
                    socketChannel.configureBlocking(false);
                    //将创建的连接添加到selector轮训器，并监听读事件
                    ByteBuffer byteBuffer = ByteBuffer.allocate(16);
                    SelectionKey scKey = socketChannel.register(selector, 0, byteBuffer);//byteBuffer做为key的附件
                    scKey.interestOps(SelectionKey.OP_READ);
                } else if (selectionKey.isReadable()) {
                    //读事件
                    SocketChannel channel = (SocketChannel) selectionKey.channel();
                    try {
                        ByteBuffer buffer = (ByteBuffer) selectionKey.attachment();
                        int read = channel.read(buffer);
                        if (read > 0) {
                            buffer.flip();
                            String s = BufferUtil.readUtf8Str(buffer);
                            System.out.println("收到数据" + s);
                            Thread.sleep(1000L);
                        } else {
                            selectionKey.cancel();
                        }
                        buffer.clear();
                    } catch (IOException e) {
                        e.printStackTrace();
                        selectionKey.cancel();
                    }
                    /*int read = channel.read(buffer);
                    if (read > 0) {
                        buffer.flip();
                        String s = BufferUtil.readUtf8Str(buffer);
                        System.out.println("收到数据" + s);
                        buffer.clear();
                    }*/
                }
            }
        }
    }

    private static void unBlock() throws Exception {
        ByteBuffer buffer = ByteBuffer.allocate(16);
        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.bind(new InetSocketAddress(8080));
        ssc.configureBlocking(false);
        List<SocketChannel> list = new ArrayList<>();
        while (true) {
            //System.out.println("accept-before");
            SocketChannel sc = ssc.accept();
            if (sc != null) {
                System.out.println("accept-after");
                list.add(sc);
                sc.configureBlocking(false);
            }
            for (SocketChannel socketChannel : list) {
                int read = socketChannel.read(buffer);
                if (read > 0) {
                    buffer.flip();
                    String s = BufferUtil.readUtf8Str(buffer);
                    System.out.println("收到数据" + s);
                    buffer.clear();
                }
            }
        }
    }

    private static void block() throws Exception {
        ByteBuffer buffer = ByteBuffer.allocate(16);
        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.bind(new InetSocketAddress(8080));
        List<SocketChannel> list = new ArrayList<>();
        while (true) {
            System.out.println("accept-before");
            SocketChannel sc = ssc.accept();
            System.out.println("accept-after");
            list.add(sc);
            for (SocketChannel socketChannel : list) {
                socketChannel.read(buffer);
                buffer.flip();
                String s = BufferUtil.readUtf8Str(buffer);
                System.out.println("收到数据" + s);
                buffer.clear();
            }
        }
    }
}
