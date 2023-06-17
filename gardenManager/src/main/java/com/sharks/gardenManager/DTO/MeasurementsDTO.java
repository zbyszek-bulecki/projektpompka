package com.sharks.gardenManager.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class MeasurementsDTO {
    private double soilMoisture;
    private double lightIntensity;
    private double temperature;
    private double pressure;
    private double waterLevel;
    private LocalDateTime createdAt;
}
