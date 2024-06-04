package com.sharks.gardenManager.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sharks.gardenManager.entities.PlanterMeasurement;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDateTime;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
public class MeasurementsDTO {
    private double soilMoisture;
    private double lightIntensity;
    private double temperature;
    private double pressure;
    private double waterLevel;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Instant createdAt;

    public static MeasurementsDTO mapToDTO(PlanterMeasurement planterMeasurement) {
        MeasurementsDTO measurementsDTO = new MeasurementsDTO();
        measurementsDTO.setSoilMoisture(planterMeasurement.getSoilMoisture());
        measurementsDTO.setLightIntensity(planterMeasurement.getLightIntensity());
        measurementsDTO.setTemperature(planterMeasurement.getTemperature());
        measurementsDTO.setPressure(planterMeasurement.getPressure());
        measurementsDTO.setWaterLevel(planterMeasurement.getWaterLevel());
        measurementsDTO.setCreatedAt(planterMeasurement.getCreatedAt());
        return measurementsDTO;
    }
}
