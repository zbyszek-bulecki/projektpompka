package com.sharks.gardenManager.service;

import com.sharks.gardenManager.DTO.SettingDTO;
import com.sharks.gardenManager.DTO.SettingUpdateDTO;
import com.sharks.gardenManager.DTO.SettingsRequestDTO;
import com.sharks.gardenManager.DTO.SettingsDTO;
import com.sharks.gardenManager.entities.Planter;
import com.sharks.gardenManager.entities.PlanterSettings;
import com.sharks.gardenManager.repositories.PlanterRepository;
import com.sharks.gardenManager.repositories.PlanterSettingsRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
        Instant previousUpdateTimestamp = settingsRequestDTO.getTimestamp();
        Optional<Planter> planter = planterRepository
                .findFirstByNameAndMacAddress(settingsRequestDTO.getName(), settingsRequestDTO.getMacAddress());
        return planter.map(p -> prepareSettingsUpdates(p, currentUpdateTimestamp, previousUpdateTimestamp))
                .orElseGet(() -> new SettingsDTO(currentUpdateTimestamp, Map.of()));
    }

    public List<SettingDTO> getSettings(String name, String macAddress) {
        Optional<Planter> planter = planterRepository
                .findFirstByNameAndMacAddress(name, macAddress);
        return planter.map(p -> getSettingsList(p, null)).orElseGet(List::of);
    }

    private SettingsDTO prepareSettingsUpdates(Planter planter, Instant currentUpdateTimestamp, Instant previousUpdateTimestamp) {
        Map<String, String> settings = getSettingsMap(planter, previousUpdateTimestamp);
        return new SettingsDTO(currentUpdateTimestamp, settings);
    }

    private Map<String, PlanterSettings> getSettingsFromDBIncludingDefaultsAndRemoveDuplicates(Planter planter, Instant previousUpdateTimestamp) {
        return fetchSettingsFromDb(planter, previousUpdateTimestamp)
                .stream()
                .collect(Collectors.toMap(PlanterSettings::getKey, ps -> ps, this::handleDuplicateSettings));
    }

    private Map<String, String> getSettingsMap(Planter planter, Instant previousUpdateTimestamp) {
        return getSettingsFromDBIncludingDefaultsAndRemoveDuplicates(planter, previousUpdateTimestamp)
                .entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().getValue()));
    }

    private List<SettingDTO> getSettingsList(Planter planter, Instant previousUpdateTimestamp) {
        return getSettingsFromDBIncludingDefaultsAndRemoveDuplicates(planter, previousUpdateTimestamp)
                .values()
                .stream()
                .map(s -> new SettingDTO(s.getKey(), s.getValue(), s.getPlanter() == null))
                .toList();
    }

    private List<PlanterSettings> fetchSettingsFromDb(Planter planter, Instant previousUpdateTimestamp) {
        return previousUpdateTimestamp == null ?
                planterSettingsRepository.findByPlanterIncludingDefaultSettings(planter) :
                planterSettingsRepository.findByPlanterAndUpdateTimestampIncludingDefaultSettings(planter, previousUpdateTimestamp);
    }

    private PlanterSettings handleDuplicateSettings(PlanterSettings s1, PlanterSettings s2) {
        return s1.getPlanter().getId() != null && s1.getValue() != null ? s1 : s2;
    }

    public boolean updateSettingsAndConfirmIfSuccessful(String name, String macAddress, Map<String, SettingUpdateDTO> settingsUpdateDTO) {

        Optional<Planter> optionalPlanter = planterRepository
                .findFirstByNameAndMacAddress(name, macAddress);
        if (optionalPlanter.isEmpty()) return false;

        Planter planter = optionalPlanter.get();
        List<PlanterSettings> settings = planterSettingsRepository.findByPlanter(planter);
        List<String> existingSettings = settings.stream().map(PlanterSettings::getKey).toList();
        Stream<PlanterSettings> updatedSettings = settings.stream()
                .filter(s -> settingsUpdateDTO.containsKey(s.getKey()))
                .peek(s -> updateExistingSetting(settingsUpdateDTO, s));
        Stream<PlanterSettings> newSettings = settingsUpdateDTO.entrySet().stream()
                .filter(s -> !existingSettings.contains(s.getKey()))
                .map(s -> new PlanterSettings(null, s.getKey(), s.getValue().value(), Instant.now(), planter));
        planterSettingsRepository.saveAll(Stream.concat(updatedSettings, newSettings).toList());
        return true;
    }

    private void updateExistingSetting(Map<String, SettingUpdateDTO> settingsUpdateDTO, PlanterSettings setting) {
        if (settingsUpdateDTO.containsKey(setting.getKey())) {
            SettingUpdateDTO update = settingsUpdateDTO.get(setting.getKey());
            setting.setValue(update.resetToDefault() ? null : update.value());
        }
    }

}
