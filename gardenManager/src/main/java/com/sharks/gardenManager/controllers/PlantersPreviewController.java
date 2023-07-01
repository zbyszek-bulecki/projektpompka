package com.sharks.gardenManager.controllers;

import com.sharks.gardenManager.DTO.PlanterDTO;
import com.sharks.gardenManager.service.PlantersPreviewService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/preview")
public class PlantersPreviewController {
    private PlantersPreviewService plantersPreviewService;

    public PlantersPreviewController(PlantersPreviewService plantersPreviewService) {
        this.plantersPreviewService = plantersPreviewService;
    }

    @GetMapping
    public List<PlanterDTO> getPlantersList() {
        return plantersPreviewService.getPlantersList();
    }

}
