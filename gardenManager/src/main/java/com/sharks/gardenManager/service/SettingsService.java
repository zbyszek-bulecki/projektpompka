package com.sharks.gardenManager.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sharks.gardenManager.DTO.CommandsRequesterDTO;
import com.sharks.gardenManager.DTO.SettingsDTO;
import com.sharks.gardenManager.entities.Planter;
import com.sharks.gardenManager.repositories.PlanterRepository;
import com.sharks.gardenManager.repositories.PlanterSettingsRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SettingsService {

    private final PlanterRepository planterRepository;
    private final PlanterSettingsRepository planterSettingsRepository;
    private final ObjectMapper objectMapper;

    public SettingsService(PlanterRepository planterRepository, PlanterSettingsRepository planterSettingsRepository, ObjectMapper objectMapper) {
        this.planterRepository = planterRepository;
        this.planterSettingsRepository = planterSettingsRepository;
        this.objectMapper = objectMapper;
    }

    public List<SettingsDTO> getAwaitingSettingsUpdates(CommandsRequesterDTO commandsRequesterDTO) {
        Optional<Planter> planter = planterRepository
                .findFirstByNameAndMacAddress(commandsRequesterDTO.getName(), commandsRequesterDTO.getMacAddress());
        return planter.map(this::prepareSettingsUpdates).orElseGet(List::of);
    }

    private List<SettingsDTO> prepareSettingsUpdates(Planter planter) {
        return planterSettingsRepository.findByPlanterAndUpdated(planter, false)
                .stream()
                .map(planterSettings -> objectMapper.convertValue(planterSettings, SettingsDTO.class))
                .toList();
    }
}
