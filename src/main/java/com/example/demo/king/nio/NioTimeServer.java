package com.example.demo.king.nio;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class NioTimeServer {
    public static void main(String[] args) throws IOException {
        int port = 8080;
        try {
            port = Integer.valueOf(args[0]);
        } catch (Exception e) {

        }
        ServerSocket server = null;
        try {
            server = new ServerSocket(port);
            System.out.println("服务端启动端口" + port);
            Socket socket = null;
            while (true) {
                socket = server.accept();
                new Thread(new NioTimeServerHandler(socket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (server != null) {
                server.close();
            }
        }
    }


}
