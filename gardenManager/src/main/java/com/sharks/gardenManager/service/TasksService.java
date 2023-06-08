package com.sharks.gardenManager.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sharks.gardenManager.DTO.CommandsRequesterDTO;
import com.sharks.gardenManager.DTO.TaskDTO;
import com.sharks.gardenManager.entities.Planter;
import com.sharks.gardenManager.entities.PlanterSettings;
import com.sharks.gardenManager.entities.PlanterTask;
import com.sharks.gardenManager.repositories.PlanterRepository;
import com.sharks.gardenManager.repositories.PlanterSettingsRepository;
import com.sharks.gardenManager.repositories.PlanterTaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

@Service
public class TasksService {

    private final PlanterRepository planterRepository;
    private final PlanterTaskRepository planterTaskRepository;
    private final PlanterSettingsRepository planterSettingsRepository;
    private final ObjectMapper objectMapper;

    public TasksService(PlanterRepository planterRepository, PlanterTaskRepository planterTaskRepository, PlanterSettingsRepository planterSettingsRepository, ObjectMapper objectMapper) {
        this.planterRepository = planterRepository;
        this.planterTaskRepository = planterTaskRepository;
        this.planterSettingsRepository = planterSettingsRepository;
        this.objectMapper = objectMapper;
    }

    public List<TaskDTO<Object>> getTasks(CommandsRequesterDTO commandsRequesterDTO) {
        Optional<Planter> planter = planterRepository
                .findByNameAndMacAddress(commandsRequesterDTO.getName(), commandsRequesterDTO.getMacAddress());
        return planter.map(value -> Stream.concat(prepareSettingsUpdates(value).stream(), getAwaitingTasks(value).stream())
                .toList()).orElseGet(List::of);
    }

    private List<TaskDTO<Object>> getAwaitingTasks(Planter planter) {
        return planterTaskRepository.findByPlanterAndFinished(planter, false)
                .stream()
                .map(task -> new TaskDTO<>(task.getTask(), convertJsonToHashMap(task.getParameters())))
                .toList();
    }

    private Object convertJsonToHashMap(String json) {
        try {
            return objectMapper.readValue(json, new TypeReference<>() {});
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    private List<TaskDTO<Object>> prepareSettingsUpdates(Planter planter) {
        return planterSettingsRepository.findByPlanterAndUpdated(planter, false)
                .stream()
                .map(this::prepareSettingsUpdate)
                .toList();
    }

    private TaskDTO<Object> prepareSettingsUpdate(PlanterSettings planterSettings) {
        return new TaskDTO<>("update_settings", planterSettings);
    }
}
