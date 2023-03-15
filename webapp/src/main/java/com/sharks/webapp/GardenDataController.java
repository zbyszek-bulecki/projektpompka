package com.sharks.webapp;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@RestController
@RequestMapping("/data")
public class GardenDataController {

    @GetMapping("/devices-list")
    public List<Device> getData(){
        return List.of(
                Device.of("adres1", DeviceStatus.RED, LocalDateTime.now().minusMinutes((int)(Math.random() * 5))),
                Device.of("adres2", DeviceStatus.GREEN, LocalDateTime.now().minusMinutes((int)(Math.random() * 5)).minusSeconds((int)(Math.random() * 50 + 1))),
                Device.of("adres3", DeviceStatus.AMBER, LocalDateTime.now().minusMinutes((int)(Math.random() * 5)).minusSeconds((int)(Math.random() * 50 + 1)))
        );
    }

    record Device(String adresMac, DeviceStatus status, LocalDateTime lastUpdate){
        public static Device of(String adresMac, DeviceStatus status, LocalDateTime lastUpdate){
            return new Device(adresMac, status, lastUpdate);
        }
    }

    public <T> T[] test(T... data){
        return data;
    }

    enum DeviceStatus{
        RED, AMBER, GREEN
    }
}
