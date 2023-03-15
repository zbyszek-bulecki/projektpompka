package com.example.doniczkaSymulator;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Slf4j
public class SimulationThread extends Thread{

    private static final String BASE_URL = "http://localhost:8080/sensors";
    private static final int SLEEP_TIME = 300000;

    private final RestTemplate restTemplate;

    @Autowired
    public SimulationThread() {
        restTemplate = new RestTemplateBuilder().errorHandler(new RestTemplateResponseErrorHandler()).build();
    }

    @Override
    public void run() {
        while(true){
            sendMeasurements();
            requestCommands();
            //responseAfterPerformingCommands
            try {
                Thread.sleep(SLEEP_TIME);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void sendMeasurements(){
        HttpEntity<Measurements> request =
                new HttpEntity<>(new Measurements("name", "XXXX", 0, 0,0,0,0, 0));
        ResponseEntity<MeasurementsResponse> response =
                restTemplate.postForEntity(BASE_URL+"/measurements", request, MeasurementsResponse.class);
    }

    record Measurements(
            String id,
            String macAddress,
            double soilMoisture,
            double temperature,
            double pressure,
            double lightIntensity,
            double waterLevel,
            double voltage
    ){}

    record MeasurementsResponse(

    ){}

    public void requestCommands(){
        ResponseEntity<Commands> response =
                restTemplate.getForEntity(BASE_URL+"/commands", Commands.class);
    }

    record Command(
            String procedureNumber,
            String[] parameters
    ){}

    record Commands(
            List<Command> commandList
    ){}
}
