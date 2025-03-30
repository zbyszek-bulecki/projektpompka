package com.sharks.gardenManager.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.Instant;

@Data
public class SettingsRequestDTO {
    private String name;
    private String macAddress;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss[.SSS]X")
    private Instant timestamp;
}
