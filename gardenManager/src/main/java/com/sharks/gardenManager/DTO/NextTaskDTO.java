package com.sharks.gardenManager.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NextTaskDTO<T> {
    private int awaitingTasks;
    private TaskDTO<T> nextTask;
}
