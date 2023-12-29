package com.sharks.gardenManager.controllers;

import com.sharks.gardenManager.DTO.MeasurementsDTO;
import com.sharks.gardenManager.DTO.PageDTO;
import com.sharks.gardenManager.DTO.PlanterDTO;
import com.sharks.gardenManager.DTO.PlanterWithLatestMeasurementDTO;
import com.sharks.gardenManager.service.PlantersPreviewService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/manager/planters")
public class PlantersManagerController {
    private final PlantersPreviewService plantersPreviewService;

    public PlantersManagerController(PlantersPreviewService plantersPreviewService) {
        this.plantersPreviewService = plantersPreviewService;
    }

    @GetMapping
    public PageDTO<List<PlanterWithLatestMeasurementDTO>> getPlantersList(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size) {
        return plantersPreviewService.getPlantersList(page, size);
    }

    @GetMapping("/{name}/{macAddress}")
    public ResponseEntity<PlanterDTO> getPlanterById(@PathVariable String name, @PathVariable String macAddress) {
        try {
            PlanterDTO planterDTO = plantersPreviewService.getPlanterByNameAndMacAddress(name, macAddress);
            return ResponseEntity.ok(planterDTO);
        }
        catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{name}/{macAddress}/measurements")
    public ResponseEntity<PageDTO<List<MeasurementsDTO>>> getMeasurementsByPlanterId(@PathVariable String name, @PathVariable String macAddress,
                                                                                    @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        try {
            PageDTO<List<MeasurementsDTO>> measurementsDTO = plantersPreviewService.getMeasurementsByPlanterNameAndMacAddress(name, macAddress, page, size);
            return ResponseEntity.ok(measurementsDTO);
        }
        catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

}
