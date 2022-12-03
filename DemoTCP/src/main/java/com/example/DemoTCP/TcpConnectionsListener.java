package com.example.DemoTCP;

import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

@Component
public class TcpConnectionsListener {
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;

    public void start(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        while(true) {
            clientSocket = serverSocket.accept();
            System.out.println("user connected");
            TcpConnectionHandler connectionHandler = new TcpConnectionHandler(clientSocket);
            connectionHandler.start();
        }
    }

    public void stop() throws IOException {
        in.close();
        out.close();
        clientSocket.close();
        serverSocket.close();
    }
}
