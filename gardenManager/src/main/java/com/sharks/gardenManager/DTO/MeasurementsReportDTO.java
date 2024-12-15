package com.sharks.gardenManager.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class MeasurementsReportDTO {
    private String name;
    private String macAddress;
    private Double soilMoisture;
    private Double lightIntensity;
    private Double temperature;
    private Double pressure;
    private Double waterLevel;
}
