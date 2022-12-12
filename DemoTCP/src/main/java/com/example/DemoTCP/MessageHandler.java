package com.example.DemoTCP;


import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class MessageHandler {
    public void handle(String data, ConnectionManager connectionHandler) throws IOException {
        String[] splited = data.split(":", 2);
        if(splited.length<2){
            return;
        }
        String key = splited[0];
        String value = splited[1];

        System.out.println("key:("+key+")");
        System.out.println("value:("+value+")");

        connectionHandler.sendMessage("response", "v1:("+value+")");
        connectionHandler.sendMessage("response", "v2:("+value+")");
    }
}
