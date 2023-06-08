package com.sharks.gardenManager.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class MeasurementsReportDTO {
    private String name;
    private String macAddress;
    private double soilMoisture;
    private double lightIntensity;
    private double temperature;
    private double pressure;
    private double waterLevel;
}
