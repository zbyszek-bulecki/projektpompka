package com.sharks.gardenManager.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sharks.gardenManager.entities.Planter;
import com.sharks.gardenManager.entities.PlanterMeasurement;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class PlanterWithLatestMeasurementDTO {
    private String name;
    private String macAddress;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Instant lastActivity;
    private double soilMoisture;
    private double lightIntensity;
    private double temperature;
    private double pressure;
    private double waterLevel;

    public static PlanterWithLatestMeasurementDTO mapToDTO(Planter planter, PlanterMeasurement planterMeasurement) {
        PlanterWithLatestMeasurementDTO planterWithLatestMeasurementDTO = new PlanterWithLatestMeasurementDTO();
        planterWithLatestMeasurementDTO.setName(planter.getName());
        planterWithLatestMeasurementDTO.setMacAddress(planter.getMacAddress());
        planterWithLatestMeasurementDTO.setLastActivity(planter.getLastActivity());
        if(planterMeasurement!=null) {
            planterWithLatestMeasurementDTO.setSoilMoisture(planterMeasurement.getSoilMoisture());
            planterWithLatestMeasurementDTO.setLightIntensity(planterMeasurement.getLightIntensity());
            planterWithLatestMeasurementDTO.setTemperature(planterMeasurement.getTemperature());
            planterWithLatestMeasurementDTO.setPressure(planterMeasurement.getPressure());
            planterWithLatestMeasurementDTO.setWaterLevel(planterMeasurement.getWaterLevel());
        }
        return planterWithLatestMeasurementDTO;
    }
}
