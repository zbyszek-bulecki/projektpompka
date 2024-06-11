package com.sharks.gardenManager.service;

import com.sharks.gardenManager.DTO.SettingsRequestDTO;
import com.sharks.gardenManager.DTO.SettingsDTO;
import com.sharks.gardenManager.entities.Planter;
import com.sharks.gardenManager.entities.PlanterSettings;
import com.sharks.gardenManager.repositories.PlanterRepository;
import com.sharks.gardenManager.repositories.PlanterSettingsRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SettingsService {

    private final PlanterRepository planterRepository;
    private final PlanterSettingsRepository planterSettingsRepository;

    public SettingsService(PlanterRepository planterRepository, PlanterSettingsRepository planterSettingsRepository) {
        this.planterRepository = planterRepository;
        this.planterSettingsRepository = planterSettingsRepository;
    }

    public SettingsDTO getAwaitingSettingsUpdates(SettingsRequestDTO settingsRequestDTO) {
        Instant currentUpdateTimestamp = Instant.now();
        Instant previousUpdateTimestamp = settingsRequestDTO.getTimestamp() == null ? Instant.MIN : settingsRequestDTO.getTimestamp();
        Optional<Planter> planter = planterRepository
                .findFirstByNameAndMacAddress(settingsRequestDTO.getName(), settingsRequestDTO.getMacAddress());
        return planter.map(p -> prepareSettingsUpdates(p, currentUpdateTimestamp, previousUpdateTimestamp))
                .orElseGet(() -> new SettingsDTO(currentUpdateTimestamp, Map.of()));
    }

    private SettingsDTO prepareSettingsUpdates(Planter planter, Instant currentUpdateTimestamp, Instant previousUpdateTimestamp) {
        Map <String, String> settings =  planterSettingsRepository.findByPlanterWithDefaultSettingsAndUpdateTimestamp(planter, previousUpdateTimestamp)
                .stream()
                .collect(Collectors.toMap(PlanterSettings::getKey, ps -> ps, this::handleDuplicateSettings))
                .entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().getValue()));
        return new SettingsDTO(currentUpdateTimestamp, settings);
    }
    private PlanterSettings handleDuplicateSettings(PlanterSettings s1, PlanterSettings s2){
        return s1.getPlanter().getId() != null ? s1 : s2;
    }
}
