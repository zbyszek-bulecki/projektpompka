package com.sharks.gardenManager.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.Instant;

@Data
public class CommandsRequesterWithTimestampDTO {
    private String name;
    private String macAddress;
    private Instant timestamp;
}
