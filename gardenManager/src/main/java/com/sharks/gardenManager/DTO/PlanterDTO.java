package com.sharks.gardenManager.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sharks.gardenManager.entities.Planter;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PlanterDTO {
    private String name;
    private String macAddress;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastActivity;

    public static PlanterDTO mapToDTO(Planter planter) {
        PlanterDTO planterDTO = new PlanterDTO();
        planterDTO.setName(planter.getName());
        planterDTO.setMacAddress(planter.getMacAddress());
        planterDTO.setLastActivity(planter.getLastActivity());
        return planterDTO;
    }
}
