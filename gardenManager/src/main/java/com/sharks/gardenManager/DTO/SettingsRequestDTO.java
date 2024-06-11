package com.sharks.gardenManager.DTO;

import lombok.Data;

import java.time.Instant;

@Data
public class SettingsRequestDTO {
    private String name;
    private String macAddress;
    private Instant timestamp;
}
