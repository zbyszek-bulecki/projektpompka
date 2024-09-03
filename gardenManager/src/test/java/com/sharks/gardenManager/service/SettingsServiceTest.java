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

        Planter planter = new Planter();
        planter.setId(null);
        planter.setName("planter_1");
        planter.setMacAddress("00:00:00:00:00:00");
        planter.setLastActivity(Instant.now().minusSeconds(6));

        planterRepository.save(planter);

        PlanterSettings planterSettings = new PlanterSettings();
        planterSettings.setId(null);
        planterSettings.setPlanter(planter);
        planterSettings.setKey("sleep_time");
        planterSettings.setValue("666");
        planterSettings.setUpdateTimestamp(Instant.now());

        planterSettingsRepository.save(planterSettings);

        PlanterSettings defaultPlanterSettings = new PlanterSettings();
        defaultPlanterSettings.setId(null);
        defaultPlanterSettings.setPlanter(null);
        defaultPlanterSettings.setKey("water_level");
        defaultPlanterSettings.setValue("100");
        defaultPlanterSettings.setUpdateTimestamp(Instant.now());

        planterSettingsRepository.save(defaultPlanterSettings);

        PlanterSettings ignoredDefaultPlanterSettings = new PlanterSettings();
        ignoredDefaultPlanterSettings.setId(null);
        ignoredDefaultPlanterSettings.setPlanter(null);
        ignoredDefaultPlanterSettings.setKey("sleep_time");
        ignoredDefaultPlanterSettings.setValue("500");
        ignoredDefaultPlanterSettings.setUpdateTimestamp(Instant.now());

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

        Planter planter = new Planter();
        planter.setId(null);
        planter.setName("planter_1");
        planter.setMacAddress("00:00:00:00:00:00");
        planter.setLastActivity(Instant.now().minusSeconds(6));

        planterRepository.save(planter);

        PlanterSettings oldPlanterSettings = new PlanterSettings();
        oldPlanterSettings.setId(null);
        oldPlanterSettings.setPlanter(planter);
        oldPlanterSettings.setKey("sleep_time");
        oldPlanterSettings.setValue("666");
        oldPlanterSettings.setUpdateTimestamp(Instant.ofEpochSecond(1718033674));

        planterSettingsRepository.save(oldPlanterSettings);

        PlanterSettings updatedPlanterSettings1 = new PlanterSettings();
        updatedPlanterSettings1.setId(null);
        updatedPlanterSettings1.setPlanter(planter);
        updatedPlanterSettings1.setKey("water_level");
        updatedPlanterSettings1.setValue("367");
        updatedPlanterSettings1.setUpdateTimestamp(Instant.ofEpochSecond(1718033675));

        planterSettingsRepository.save(updatedPlanterSettings1);

        PlanterSettings updatedPlanterSettings2 = new PlanterSettings();
        updatedPlanterSettings2.setId(null);
        updatedPlanterSettings2.setPlanter(planter);
        updatedPlanterSettings2.setKey("light_multiplier");
        updatedPlanterSettings2.setValue("34");
        updatedPlanterSettings2.setUpdateTimestamp(Instant.ofEpochSecond(1718033679));

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
        Planter planter = new Planter();
        planter.setId(null);
        planter.setName("planter_1");
        planter.setMacAddress("00:00:00:00:00:00");
        planter.setLastActivity(Instant.now());

        planterRepository.save(planter);

        PlanterSettings planterSettings = new PlanterSettings();
        planterSettings.setId(null);
        planterSettings.setPlanter(planter);
        planterSettings.setKey("sleep_time");
        planterSettings.setValue("666");
        planterSettings.setUpdateTimestamp(Instant.now());

        planterSettingsRepository.save(planterSettings);

        PlanterSettings defaultPlanterSettings = new PlanterSettings();
        defaultPlanterSettings.setId(null);
        defaultPlanterSettings.setPlanter(null);
        defaultPlanterSettings.setKey("water_level");
        defaultPlanterSettings.setValue("367");
        defaultPlanterSettings.setUpdateTimestamp(Instant.now());

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

        Planter planter = new Planter();
        planter.setId(null);
        planter.setName("planter_1");
        planter.setMacAddress("00:00:00:00:00:00");
        planter.setLastActivity(Instant.now());

        planterRepository.save(planter);

        Map<String, SettingUpdateDTO> settingsToBeUpdated = Map.of(
                "water_level", new SettingUpdateDTO("987", false)
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
    void testSettingService_WhenUpdatingExistingParameter() {

        //Given

        Planter planter = new Planter();
        planter.setId(null);
        planter.setName("planter_1");
        planter.setMacAddress("00:00:00:00:00:00");
        planter.setLastActivity(Instant.now());

        planterRepository.save(planter);

        PlanterSettings planterSettings = new PlanterSettings();
        planterSettings.setId(null);
        planterSettings.setPlanter(planter);
        planterSettings.setKey("sleep_time");
        planterSettings.setValue("666");
        planterSettings.setUpdateTimestamp(Instant.now());

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

        Planter planter = new Planter();
        planter.setId(null);
        planter.setName("planter_1");
        planter.setMacAddress("00:00:00:00:00:00");
        planter.setLastActivity(Instant.now());

        planterRepository.save(planter);

        PlanterSettings defaultPlanterSettings = new PlanterSettings();
        defaultPlanterSettings.setId(null);
        defaultPlanterSettings.setPlanter(null);
        defaultPlanterSettings.setKey("water_level");
        defaultPlanterSettings.setValue("367");
        defaultPlanterSettings.setUpdateTimestamp(Instant.now());

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

        Planter planter = new Planter();
        planter.setId(null);
        planter.setName("planter_1");
        planter.setMacAddress("00:00:00:00:00:00");
        planter.setLastActivity(Instant.now());

        planterRepository.save(planter);

        PlanterSettings planterSettings = new PlanterSettings();
        planterSettings.setId(null);
        planterSettings.setPlanter(planter);
        planterSettings.setKey("water_level");
        planterSettings.setValue("666");
        planterSettings.setUpdateTimestamp(Instant.now());

        planterSettingsRepository.save(planterSettings);

        PlanterSettings defaultPlanterSettings = new PlanterSettings();
        defaultPlanterSettings.setId(null);
        defaultPlanterSettings.setPlanter(null);
        defaultPlanterSettings.setKey("water_level");
        defaultPlanterSettings.setValue("367");
        defaultPlanterSettings.setUpdateTimestamp(Instant.now());

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
}