package com.example.planterSimulator;

import com.jogamp.common.util.InterruptSource;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
public class SimulationThread extends Thread{

    private static final String BASE_URL = "http://localhost:8080";
    private static final int SLEEP_TIME = 300000;
    private final String name;
    private final String macAddress;

    private final RestTemplate restTemplate;

    @Autowired
    public SimulationThread(String name, String macAddress) {
        restTemplate = new RestTemplateBuilder().errorHandler(new RestTemplateResponseErrorHandler()).build();
        this.name = name;
        this.macAddress = macAddress;

        MappingJackson2HttpMessageConverter converter =
                new MappingJackson2HttpMessageConverter();
        converter.setSupportedMediaTypes(Arrays.asList(
                MediaType.APPLICATION_OCTET_STREAM));
        restTemplate.getMessageConverters().add(converter);
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
                new HttpEntity<>(new Measurements(name, macAddress, randomDouble(), randomDouble(),randomDouble(),randomDouble(),randomDouble()));
        ResponseEntity<Void> response =
                restTemplate.postForEntity(BASE_URL+"/planters/measurements", request, Void.class);
    }

    private double randomDouble(){
        return ThreadLocalRandom.current().nextDouble(2d);
    }

    record Measurements(
            String name,
            String macAddress,
            double soilMoisture,
            double temperature,
            double pressure,
            double lightIntensity,
            double waterLevel
    ){}

    record MeasurementsResponse(

    ){}

    public void requestCommands(){
        ResponseEntity<Commands> response =
                restTemplate.getForEntity(BASE_URL+"/tasks", Commands.class);
    }

    record Command(
            String procedureNumber,
            String[] parameters
    ){}

    record Commands(
            List<Command> commandList
    ){}
}
