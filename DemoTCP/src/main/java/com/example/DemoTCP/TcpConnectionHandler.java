package com.example.DemoTCP;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;


public class TcpConnectionHandler extends Thread {
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;

    public TcpConnectionHandler(Socket clientSocket) throws IOException {
        this.clientSocket = clientSocket;
        out = new PrintWriter(clientSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    }

    @Override
    public void run() {
        try {
            while(true) {
                String data = in.readLine();
                System.out.println("msg: " + data);
                out.println("echo: " + data);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
