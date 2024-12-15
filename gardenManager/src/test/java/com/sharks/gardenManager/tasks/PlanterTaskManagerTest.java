package com.sharks.gardenManager.tasks;

import com.sharks.gardenManager.TestContainersBase;
import com.sharks.gardenManager.entities.Planter;
import com.sharks.gardenManager.entities.PlanterMeasurement;
import com.sharks.gardenManager.entities.PlanterSettings;
import com.sharks.gardenManager.entities.PlanterTask;
import com.sharks.gardenManager.repositories.PlanterMeasurementRepository;
import com.sharks.gardenManager.repositories.PlanterRepository;
import com.sharks.gardenManager.repositories.PlanterSettingsRepository;
import com.sharks.gardenManager.repositories.PlanterTaskRepository;
import com.sharks.gardenManager.service.SettingsService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest
@Transactional
class PlanterTaskManagerTest extends TestContainersBase {
    @Autowired
    private PlanterSettingsRepository planterSettingsRepository;
    @Autowired
    private PlanterTaskRepository planterTaskRepository;
    @Autowired
    private PlanterRepository planterRepository;
    @Autowired
    private PlanterMeasurementRepository planterMeasurementRepository;

    @BeforeEach
    void setUp() {
        planterRepository.deleteAll();
        planterTaskRepository.deleteAll();
        planterSettingsRepository.deleteAll();
        planterMeasurementRepository.deleteAll();
    }

    @Test
    void run() {

        //Given
        Planter planter = new Planter();

        planter.setId(null);
        planter.setName("planter_1");
        planter.setMacAddress("00:00:00:00:00:00");
        planter.setLastActivity(Instant.now());

        planterRepository.save(planter);

        PlanterMeasurement measurementOld = new PlanterMeasurement();
        measurementOld.setPlanter(planter);
        measurementOld.setId(null);
        measurementOld.setWaterLevel(48.0);
        measurementOld.setCreatedAt(Instant.now().minus(1, ChronoUnit.SECONDS));
        planterMeasurementRepository.save(measurementOld);

        PlanterMeasurement measurementLatest = new PlanterMeasurement();
        measurementLatest.setPlanter(planter);
        measurementLatest.setId(null);
        measurementLatest.setWaterLevel(44.7);
        measurementLatest.setCreatedAt(Instant.now());
        planterMeasurementRepository.save(measurementLatest);

        var test1 = planterMeasurementRepository.findAll();
        var test2 = planterMeasurementRepository.findLastMeasurement(planter);

        Map<String, PlanterSettings> settingsMap = Map.of("watering_threshold", new PlanterSettings(null, "watering_threshold", "46", Instant.now(), planter));
        SettingsService settingsService = Mockito.mock(SettingsService.class);
        when(settingsService.getSettingsFromDBIncludingDefaultsAndRemoveDuplicates(planter, null)).thenReturn(settingsMap);

        List<TaskLogic> taskLogicList = List.of(new WateringTaskLogic());

        PlanterTaskManager objectUnderTest = new PlanterTaskManager(planterRepository, planterMeasurementRepository, planterTaskRepository, settingsService, taskLogicList);

        //When
        objectUnderTest.run();
        objectUnderTest.run();

        //Then
        List<PlanterTask> actual = planterTaskRepository.findAll();
        assertThat(actual.size()).isEqualTo(1);
        assertThat(actual).extracting(PlanterTask::getTask).containsExactlyInAnyOrder("Watering");
    }
}