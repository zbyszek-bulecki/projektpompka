package com.sharks.gardenManager.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TaskDTO<T> {
    private String command;
    private T parameters;
}
