package com.sharks.gardenManager.controllers;

import com.sharks.gardenManager.DTO.MeasurementsReportDTO;
import com.sharks.gardenManager.service.MeasurementService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/planters")
public class PlanterController {

    MeasurementService measurementService;

    public PlanterController(MeasurementService measurementService) {
        this.measurementService = measurementService;
    }

    @PostMapping("/measurements")
    public ResponseEntity<Void> post(@RequestBody MeasurementsReportDTO measurementsDTO){
        measurementService.registerMeasurements(measurementsDTO);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public String test() {
        return "test";
    }
}
