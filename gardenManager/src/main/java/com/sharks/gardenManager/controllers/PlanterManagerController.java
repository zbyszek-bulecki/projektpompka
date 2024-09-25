package com.sharks.gardenManager.controllers;

import com.sharks.gardenManager.DTO.*;
import com.sharks.gardenManager.service.PlantersPreviewService;
import com.sharks.gardenManager.service.SettingsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/manager/planters")
public class PlanterManagerController {
    private final PlantersPreviewService plantersPreviewService;
    private final SettingsService settingsService;

    public PlanterManagerController(PlantersPreviewService plantersPreviewService, SettingsService settingsService) {
        this.plantersPreviewService = plantersPreviewService;
        this.settingsService = settingsService;
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

    @GetMapping("/{name}/{macAddress}/settings")
    public List<SettingDTO> getSettingsListByPlanterId (@PathVariable String name, @PathVariable String macAddress) {
        return settingsService.getSettings(name, macAddress);
    }

    @PostMapping("/{name}/{macAddress}/settings")
    public ResponseEntity<Void> postSettingsListByPlanterId (@PathVariable String name, @PathVariable String macAddress, @RequestBody Map <String, SettingUpdateDTO> settingsUpdateDTO) {
        if(settingsService.updateSettingsAndConfirmIfSuccessful(name, macAddress, settingsUpdateDTO))
            return ResponseEntity.ok().build();
        return ResponseEntity.notFound().build();
    }

}