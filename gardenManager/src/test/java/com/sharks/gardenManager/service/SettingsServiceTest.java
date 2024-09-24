package com.sharks.gardenManager.service;

import com.sharks.gardenManager.DTO.SettingDTO;
import com.sharks.gardenManager.DTO.SettingUpdateDTO;
import com.sharks.gardenManager.DTO.SettingsRequestDTO;
import com.sharks.gardenManager.DTO.SettingsDTO;
import com.sharks.gardenManager.TestContainersBase;
import com.sharks.gardenManager.entities.Planter;
import com.sharks.gardenManager.entities.PlanterSettings;
import com.sharks.gardenManager.repositories.PlanterRepository;
import com.sharks.gardenManager.repositories.PlanterSettingsRepository;
import jakarta.transaction.Transactional;
import org.assertj.core.api.Assertions;
import org.assertj.core.groups.Tuple;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Instant;
import java.util.List;
import java.util.Map;


@SpringBootTest
@Transactional
class SettingsServiceTest extends TestContainersBase {

    @Autowired
    private PlanterSettingsRepository planterSettingsRepository;

    @Autowired
    private PlanterRepository planterRepository;

    private SettingsService objectUnderTest;

    @BeforeEach
    void setUp() {
        planterRepository.deleteAll();
        planterSettingsRepository.deleteAll();
        objectUnderTest = new SettingsService(planterRepository, planterSettingsRepository);
    }

    @Test
    void testSettingsService_WhenFetchingAllSettings() {

        Planter planter = createNewPlanter();

        planterRepository.save(planter);

        PlanterSettings planterSettings = getSettings(planter, "sleep_time", "666", Instant.now());

        planterSettingsRepository.save(planterSettings);

        PlanterSettings defaultPlanterSettings = getSettings(null, "water_level", "100", Instant.now());

        planterSettingsRepository.save(defaultPlanterSettings);

        PlanterSettings ignoredDefaultPlanterSettings = getSettings(null, "sleep_time", "500", Instant.now());

        planterSettingsRepository.save(ignoredDefaultPlanterSettings);

        SettingsRequestDTO settingsRequestDTO = new SettingsRequestDTO();
        settingsRequestDTO.setName(planter.getName());
        settingsRequestDTO.setMacAddress(planter.getMacAddress());
        settingsRequestDTO.setTimestamp(null);

        //When
        SettingsDTO settingsDTO = objectUnderTest.getAwaitingSettingsUpdates(settingsRequestDTO);

        //Then
        Assertions.assertThat(settingsDTO.settings().size()).isEqualTo(2);
        Assertions.assertThat(settingsDTO.settings().entrySet())
                .extracting(s -> Tuple.tuple(s.getKey(), s.getValue()))
                .containsExactlyInAnyOrder(
                        Tuple.tuple(planterSettings.getKey(), planterSettings.getValue()),
                        Tuple.tuple(defaultPlanterSettings.getKey(), defaultPlanterSettings.getValue())
                );
    }

    @Test
    void testSettingsService_WhenFetchingOnlyUpdates() {

        Planter planter = createNewPlanter();

        planterRepository.save(planter);

        PlanterSettings oldPlanterSettings = getSettings(planter, "sleep_time", "666", Instant.ofEpochSecond(1718033674));

        planterSettingsRepository.save(oldPlanterSettings);

        PlanterSettings updatedPlanterSettings1 = getSettings(planter, "water_level", "367", Instant.ofEpochSecond(1718033675));

        planterSettingsRepository.save(updatedPlanterSettings1);

        PlanterSettings updatedPlanterSettings2 = getSettings(planter, "light_multiplier", "34", Instant.ofEpochSecond(1718033679));

        planterSettingsRepository.save(updatedPlanterSettings2);

        SettingsRequestDTO settingsRequestDTO = new SettingsRequestDTO();
        settingsRequestDTO.setName(planter.getName());
        settingsRequestDTO.setMacAddress(planter.getMacAddress());
        settingsRequestDTO.setTimestamp(Instant.ofEpochSecond(1718033675));

        //When
        SettingsDTO settingsDTO = objectUnderTest.getAwaitingSettingsUpdates(settingsRequestDTO);

        //Then
        Assertions.assertThat(settingsDTO.settings().size()).isEqualTo(2);
        Assertions.assertThat(settingsDTO.settings().entrySet())
                .extracting(s -> Tuple.tuple(s.getKey(), s.getValue()))
                .containsExactlyInAnyOrder(
                        Tuple.tuple(updatedPlanterSettings1.getKey(), updatedPlanterSettings1.getValue()),
                        Tuple.tuple(updatedPlanterSettings2.getKey(), updatedPlanterSettings2.getValue())
                );
    }

    @Test
    void testSettingService_WhenFetchingUserSettings() {

        //Given
        Planter planter = createNewPlanter();

        planterRepository.save(planter);

        PlanterSettings planterSettings = getSettings(planter, "sleep_time", "666", Instant.now());

        planterSettingsRepository.save(planterSettings);

        PlanterSettings defaultPlanterSettings = getSettings(null, "water_level", "367", Instant.now());

        planterSettingsRepository.save(defaultPlanterSettings);

        //When
        List<SettingDTO> settingList = objectUnderTest.getSettings(planter.getName(), planter.getMacAddress());

        //Then
        Assertions.assertThat(settingList).size().isEqualTo(2);
        Assertions.assertThat(settingList)
                .extracting(s -> Tuple.tuple(s.key(), s.value(), s.isDefault()))
                .containsExactlyInAnyOrder(
                        Tuple.tuple(defaultPlanterSettings.getKey(), defaultPlanterSettings.getValue(), defaultPlanterSettings.getPlanter() == null),
                        Tuple.tuple(planterSettings.getKey(), planterSettings.getValue(), planterSettings.getPlanter() == null)
                );
    }

    @Test
    void testSettingService_WhenCreatingSettings() {

        //Given

        Planter planter = createNewPlanter();

        planterRepository.save(planter);

        Map<String, SettingUpdateDTO> settingsToBeUpdated = getSettingsToBeUpdated();

        //When
        boolean result = objectUnderTest.updateSettingsAndConfirmIfSuccessful(planter.getName(), planter.getMacAddress(), settingsToBeUpdated);

        //Then
        Assertions.assertThat(result).isTrue();
        List<PlanterSettings> actualSettings = planterSettingsRepository.findByPlanter(planter);
        List<Tuple> expectedSettings = settingsToBeUpdated.entrySet()
                .stream()
                .map(s -> Tuple.tuple(s.getKey(), s.getValue().resetToDefault() ? null : s.getValue().value()))
                .toList();
        Assertions.assertThat(actualSettings).extracting(s -> Tuple.tuple(s.getKey(), s.getValue()))
                .containsExactlyInAnyOrderElementsOf(expectedSettings);
    }

    @NotNull
    private static Map<String, SettingUpdateDTO> getSettingsToBeUpdated() {
        return Map.of(
                "water_level", new SettingUpdateDTO("987", false)
        );
    }

    @Test
    void testSettingService_WhenUpdatingExistingParameter() {

        //Given

        Planter planter = createNewPlanter();

        planterRepository.save(planter);

        PlanterSettings planterSettings = getSettings(planter, "sleep_time", "666", Instant.now());

        planterSettingsRepository.save(planterSettings);

        Map<String, SettingUpdateDTO> settingsToBeUpdated = Map.of(
                "sleep_time", new SettingUpdateDTO("987", false)
        );

        //When
        boolean result = objectUnderTest.updateSettingsAndConfirmIfSuccessful(planter.getName(), planter.getMacAddress(), settingsToBeUpdated);

        //Then
        Assertions.assertThat(result).isTrue();
        List<PlanterSettings> actualSettings = planterSettingsRepository.findByPlanter(planter);
        List<Tuple> expectedSettings = settingsToBeUpdated.entrySet()
                .stream()
                .map(s -> Tuple.tuple(s.getKey(), s.getValue().resetToDefault() ? null : s.getValue().value()))
                .toList();
        Assertions.assertThat(actualSettings).extracting(s -> Tuple.tuple(s.getKey(), s.getValue()))
                .containsExactlyInAnyOrderElementsOf(expectedSettings);
    }

    @Test
    void testSettingService_WhenOverwritingDefaultParameter() {

        //Given

        Planter planter = createNewPlanter();

        planterRepository.save(planter);

        PlanterSettings defaultPlanterSettings = getSettings(null, "water_level", "367", Instant.now());

        planterSettingsRepository.save(defaultPlanterSettings);

        Map<String, SettingUpdateDTO> settingsToBeUpdated = Map.of(
                "water_level", new SettingUpdateDTO("987", false)
        );

        //When
        boolean result = objectUnderTest.updateSettingsAndConfirmIfSuccessful(planter.getName(), planter.getMacAddress(), settingsToBeUpdated);
        List<SettingDTO> actualSettings = objectUnderTest.getSettings(planter.getName(), planter.getMacAddress());

        //Then
        Assertions.assertThat(result).isTrue();
        List<Tuple> expectedSettings = settingsToBeUpdated.entrySet()
                .stream()
                .map(s -> Tuple.tuple(s.getKey(), s.getValue().resetToDefault() ? null : s.getValue().value(), false))
                .toList();
        Assertions.assertThat(actualSettings).extracting(s -> Tuple.tuple(s.key(), s.value(), s.isDefault()))
                .containsExactlyInAnyOrderElementsOf(expectedSettings);

    }

    @Test
    void testSettingService_WhenResettingExistingParameterToDefault() {

        //Given

        Planter planter = createNewPlanter();

        planterRepository.save(planter);

        PlanterSettings planterSettings = getSettings(planter, "water_level", "666", Instant.now());

        planterSettingsRepository.save(planterSettings);

        PlanterSettings defaultPlanterSettings = getSettings(null, "water_level", "367", Instant.now());

        planterSettingsRepository.save(defaultPlanterSettings);

        Map<String, SettingUpdateDTO> settingsToBeUpdated = Map.of(
                "water_level", new SettingUpdateDTO("987", true)
        );

        //When
        boolean result = objectUnderTest.updateSettingsAndConfirmIfSuccessful(planter.getName(), planter.getMacAddress(), settingsToBeUpdated);
        List<SettingDTO> actualSettings = objectUnderTest.getSettings(planter.getName(), planter.getMacAddress());
        //Then
        Assertions.assertThat(result).isTrue();
        Assertions.assertThat(actualSettings).extracting(s -> Tuple.tuple(s.key(), s.value(), s.isDefault()))
                .containsExactlyInAnyOrder(Tuple.tuple(defaultPlanterSettings.getKey(), defaultPlanterSettings.getValue(), true));
    }

    @NotNull
    private static Planter createNewPlanter() {
        Planter planter = new Planter();
        planter.setId(null);
        planter.setName("planter_1");
        planter.setMacAddress("00:00:00:00:00:00");
        planter.setLastActivity(Instant.now());
        return planter;
    }

    @NotNull
    private static PlanterSettings getSettings(Planter planter, String sleep_time, String value, Instant now) {
        PlanterSettings planterSettings = new PlanterSettings();
        planterSettings.setId(null);
        planterSettings.setPlanter(planter);
        planterSettings.setKey(sleep_time);
        planterSettings.setValue(value);
        planterSettings.setUpdateTimestamp(now);
        return planterSettings;
    }

}