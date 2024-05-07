package com.sharks.gardenManager.controllers;

import com.sharks.gardenManager.DTO.*;
import com.sharks.gardenManager.service.MeasurementService;
import com.sharks.gardenManager.service.SettingsService;
import com.sharks.gardenManager.service.TasksService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/planter")
public class PlanterController {

    private final MeasurementService measurementService;
    private final TasksService tasksService;
    private final SettingsService settingsService;

    public PlanterController(MeasurementService measurementService, TasksService tasksService, SettingsService settingsService) {
        this.measurementService = measurementService;
        this.tasksService = tasksService;
        this.settingsService = settingsService;
    }

    @GetMapping
    public ResponseEntity<Void> get(){
        return ResponseEntity.ok().build();
    }

    @PostMapping("/measurements")
    public ResponseEntity<Void> consumeMeasurements(@RequestBody MeasurementsReportDTO measurementsDTO){
        measurementService.registerMeasurements(measurementsDTO);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/next_task")
    public NextTaskDTO<Object> getNextTask(@RequestBody CommandsRequesterDTO commandsRequesterDTO){
        return tasksService.getNextTask(commandsRequesterDTO);
    }

    @PostMapping("/settings_updates")
    public SettingsDTO getSettingsUpdates(@RequestBody CommandsRequesterWithTimestampDTO commandsRequesterWithTimestampDTO){
        return settingsService.getAwaitingSettingsUpdates(commandsRequesterWithTimestampDTO);
    }
}

//TODO: add timestamp to PlanterController to take timestamp from planter
