package com.example.DemoTCP.service;

import com.example.DemoTCP.entity.Sensors;
import com.example.DemoTCP.repository.SensorsRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SensorsService {


    private SensorsRepository sensorsRepository;
    private RestTemplate restTemplate;


    public SensorsService(SensorsRepository sensorsRepository, RestTemplate restTemplate) {
        this.sensorsRepository = sensorsRepository;
        this.restTemplate = restTemplate;
    }

    public List<Sensors> getAllSensors() {
        return sortSensors(sensorsRepository.getAllSensors());
    }

    public List<Sensors> getSensorsByAddress(String address) {
        return sortSensors(sensorsRepository.getSensorsByAddress(address));
    }

    public void addSensors(String json) {
        ObjectMapper mapper = new ObjectMapper();
        Sensors value = null;
        try {
            value = mapper.readValue(json, Sensors.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        addSensors(value);
    }
    public void addSensors(Sensors sensors) {
        sensorsRepository.save(sensors);
    }

    private List<Sensors> sortSensors(List<Sensors> sensors) {
        return sensors.stream()
                .sorted((o1,o2) -> o2.getDate().compareTo(o1.getDate()))
                .collect(Collectors.toList());
    }

}
