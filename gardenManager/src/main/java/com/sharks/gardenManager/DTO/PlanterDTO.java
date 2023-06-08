package com.sharks.gardenManager.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PlanterDTO {
    private String name;
    private String macAddress;
    private LocalDateTime lastActivity;
    private List<MeasurementsDTO> planterMeasurement;
}
