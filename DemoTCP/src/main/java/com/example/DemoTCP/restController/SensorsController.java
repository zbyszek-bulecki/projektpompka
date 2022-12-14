package com.example.DemoTCP.restController;

import com.example.DemoTCP.service.SensorsService;
import com.example.DemoTCP.entity.Sensors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/")
public class SensorsController {

    SensorsService sensorsService;

    public SensorsController(SensorsService sensorsService) {
        this.sensorsService = sensorsService;
    }

    @GetMapping()
    public List<Sensors> getAllSensors() {
        return sensorsService.getAllSensors();
    }

    @GetMapping("/{address}")
    public List<Sensors> getSensors(@PathVariable(value="address") String address) {
        return sensorsService.getSensorsByAddress(address);
    }
}
