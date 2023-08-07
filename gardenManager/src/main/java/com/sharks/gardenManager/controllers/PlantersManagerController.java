package com.sharks.gardenManager.controllers;

import com.sharks.gardenManager.DTO.PlanterDTO;
import com.sharks.gardenManager.service.PlantersPreviewService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public List<PlanterDTO> getPlantersList() {
        return plantersPreviewService.getPlantersList();
    }

    @GetMapping("/{name}_{macAddress}")
    public ResponseEntity<PlanterDTO> getPlanterById(@PathVariable String name, @PathVariable String macAddress) {
        try {
            PlanterDTO planterDTO = plantersPreviewService.getPlanterByNameAndMacAddress(name, macAddress);
            return ResponseEntity.ok(planterDTO);
        }
        catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

}
