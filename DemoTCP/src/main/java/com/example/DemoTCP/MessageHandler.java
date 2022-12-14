package com.example.DemoTCP;


import com.example.DemoTCP.service.SensorsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class MessageHandler {

    @Autowired
    private SensorsService sensorsService;
    public void handle(String data, ConnectionManager connectionHandler) throws IOException {
        String[] splited = data.split(":", 2);
        if(splited.length<2){
            return;
        }
        String key = splited[0];
        String value = splited[1];

        System.out.println("key:("+key+")");
        System.out.println("value:("+value+")");

        if (key.equals("sensors")) {
            sensorsService.addSensors(value);
        }

        connectionHandler.sendMessage("response", "v1:("+value+")");
        connectionHandler.sendMessage("response", "v2:("+value+")");
    }
}
