package com.sharks.gardenManager.controllers;

import com.sharks.gardenManager.DTO.*;
import com.sharks.gardenManager.service.MeasurementService;
import com.sharks.gardenManager.service.SettingsService;
import com.sharks.gardenManager.service.TasksService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/planter")
public class PlanterDeviceController {

    private final MeasurementService measurementService;
    private final TasksService tasksService;
    private final SettingsService settingsService;

    public PlanterDeviceController(MeasurementService measurementService, TasksService tasksService, SettingsService settingsService) {
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
    public NextTaskDTO<Object> getNextTask(@RequestBody CommandsRequestDTO commandsRequestDTO){
        return tasksService.getNextTask(commandsRequestDTO);
    }

    @PostMapping("/settings_updates")
    public SettingsDTO getSettingsUpdates(@RequestBody SettingsRequestDTO settingsRequestDTO){
        return settingsService.getAwaitingSettingsUpdates(settingsRequestDTO);
    }
}