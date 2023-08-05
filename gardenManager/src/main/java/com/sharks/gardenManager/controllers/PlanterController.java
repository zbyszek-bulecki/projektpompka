package com.sharks.gardenManager.controllers;

import com.sharks.gardenManager.DTO.CommandsRequesterDTO;
import com.sharks.gardenManager.DTO.MeasurementsReportDTO;
import com.sharks.gardenManager.DTO.TaskDTO;
import com.sharks.gardenManager.service.MeasurementService;
import com.sharks.gardenManager.service.TasksService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/planter")
public class PlanterController {

    private final MeasurementService measurementService;
    private final TasksService tasksService;

    public PlanterController(MeasurementService measurementService, TasksService tasksService) {
        this.measurementService = measurementService;
        this.tasksService = tasksService;
    }

    @GetMapping
    public ResponseEntity<Void> get(){
        return ResponseEntity.ok().build();
    }

    @PostMapping("/measurements")
    public ResponseEntity<Void> post(@RequestBody MeasurementsReportDTO measurementsDTO){
        measurementService.registerMeasurements(measurementsDTO);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/tasks")
    public List<TaskDTO<Object>> getTasks(@RequestBody CommandsRequesterDTO commandsRequesterDTO){
        return tasksService.getTasks(commandsRequesterDTO);
    }
}
