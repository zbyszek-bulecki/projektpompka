package com.sharks.gardenManager.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sharks.gardenManager.DTO.PlanterDTO;
import com.sharks.gardenManager.repositories.PlanterRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlantersPreviewService {
    private PlanterRepository planterRepository;
    private ObjectMapper objectMapper;

    public PlantersPreviewService(PlanterRepository planterRepository, ObjectMapper objectMapper) {
        this.planterRepository = planterRepository;
        this.objectMapper = objectMapper;
    }

    public List<PlanterDTO> getPlantersList() {
        return planterRepository.findAll().stream()
                .map(planter -> objectMapper.convertValue(planter, PlanterDTO.class))
                .toList();
    }

    public PlanterDTO getPlanterByNameAndMacAddress(String name, String macAddress) {
        return planterRepository.findByNameAndMacAddress(name, macAddress)
                .map(planter -> objectMapper.convertValue(planter, PlanterDTO.class))
                .orElseThrow();
    }
}
