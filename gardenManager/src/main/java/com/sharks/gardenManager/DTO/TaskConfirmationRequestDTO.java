package com.sharks.gardenManager.DTO;

import lombok.Data;

@Data
public class TaskConfirmationRequestDTO {
    private String name;
    private String macAddress;
    private String command;
}
