package com.sharks.gardenManager.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sharks.gardenManager.DTO.CommandsRequesterDTO;
import com.sharks.gardenManager.DTO.TaskDTO;
import com.sharks.gardenManager.TestContainersBase;
import com.sharks.gardenManager.entities.Planter;
import com.sharks.gardenManager.entities.PlanterSettings;
import com.sharks.gardenManager.entities.PlanterTask;
import com.sharks.gardenManager.repositories.PlanterMeasurementRepository;
import com.sharks.gardenManager.repositories.PlanterRepository;
import com.sharks.gardenManager.repositories.PlanterSettingsRepository;
import com.sharks.gardenManager.repositories.PlanterTaskRepository;
import jakarta.transaction.Transactional;
import org.assertj.core.api.Assertions;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@Transactional
class TasksServiceTest extends TestContainersBase {
    @Autowired
    private PlanterSettingsRepository planterSettingsRepository;
    @Autowired
    private PlanterTaskRepository planterTaskRepository;
    @Autowired
    private PlanterRepository planterRepository;
    @Autowired
    private ObjectMapper objectMapper;

    private TasksService objectUnderTest;

    @BeforeEach
    void setUp() {
        planterRepository.deleteAll();
        planterTaskRepository.deleteAll();
        planterSettingsRepository.deleteAll();
        objectUnderTest = new TasksService(planterRepository, planterTaskRepository, planterSettingsRepository, objectMapper);
    }

    @Test
    void testTaskService(){
        //Given
        PlanterTestInstance planterTestInstance = new PlanterTestInstance("planter_1", "00:00:00:00:00:01");
        CommandsRequesterDTO commandsRequesterDTO = prepareCommandsRequesterDTO(planterTestInstance);
        Planter planter = savePlanter(planterTestInstance);

        saveSetting(planter,"ssid","ssid012345", true);
        saveSetting(planter,"sleep","1000", false);

        saveTasks(planter, "watering", "{}", false);
        saveTasks(planter, "other", "{}", true);

        //When
        List<TaskDTO<Object>> tasks = objectUnderTest.getTasks(commandsRequesterDTO);

        //Then
        Assertions.assertThat(tasks).hasSize(2);
        Assertions.assertThat(tasks).extracting(TaskDTO::getCommand).containsExactlyInAnyOrder("watering", "update_settings");

    }

    private PlanterTask saveTasks(Planter planter, String task, String parameters, boolean finished) {
        PlanterTask planterTask = new PlanterTask();
        planterTask.setPlanter(planter);
        planterTask.setTask(task);
        planterTask.setParameters(parameters);
        planterTask.setFinished(finished);
        return planterTaskRepository.save(planterTask);
    }

    private PlanterSettings saveSetting(Planter planter, String settingKey, String settingValue, boolean updated) {
        PlanterSettings planterSettings = new PlanterSettings();
        planterSettings.setPlanter(planter);
        planterSettings.setSettingKey(settingKey);
        planterSettings.setSettingValue(settingValue);
        planterSettings.setUpdated(updated);
        return planterSettingsRepository.save(planterSettings);
    }

    private Planter savePlanter(PlanterTestInstance planterTestInstance) {
        Planter planter = new Planter();
        planter.setName(planterTestInstance.name());
        planter.setMacAddress(planterTestInstance.macAddress());
        planter.setLastActivity(LocalDateTime.now());
        return planterRepository.save(planter);
    }

    @NotNull
    private static CommandsRequesterDTO prepareCommandsRequesterDTO(PlanterTestInstance planterTestInstance) {
        CommandsRequesterDTO commandsRequesterDTO = new CommandsRequesterDTO();
        commandsRequesterDTO.setName(planterTestInstance.name());
        commandsRequesterDTO.setMacAddress(planterTestInstance.macAddress());
        return commandsRequesterDTO;
    }


    private record PlanterTestInstance(String name, String macAddress){}
}