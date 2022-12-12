package com.example.DemoTCP;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

@Component
public class TcpConnectionsListener {
    private TcpConnectionThreadPool tcpConnectionThreadPool;
    private MessageHandler messageHandler;
    private ServerSocket serverSocket;
    private Socket clientSocket;

    public TcpConnectionsListener(TcpConnectionThreadPool tcpConnectionThreadPool, MessageHandler messageHandler) {
        this.tcpConnectionThreadPool = tcpConnectionThreadPool;
        this.messageHandler = messageHandler;
    }

    public void start(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        while(true) {
            clientSocket = serverSocket.accept();
            clientSocket.setTcpNoDelay(true);
            System.out.println("user connected");
            ConnectionManager connectionHandler = new ConnectionManager(clientSocket, messageHandler);
            connectionHandler.start(tcpConnectionThreadPool);
        }
    }

    public void stop() throws IOException {
        serverSocket.close();
    }
}
