package com.sharks.gardenManager.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sharks.gardenManager.DTO.CommandsRequesterDTO;
import com.sharks.gardenManager.DTO.NextTaskDTO;
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
    private final ObjectMapper objectMapper;

    public TasksService(PlanterRepository planterRepository, PlanterTaskRepository planterTaskRepository, ObjectMapper objectMapper) {
        this.planterRepository = planterRepository;
        this.planterTaskRepository = planterTaskRepository;
        this.objectMapper = objectMapper;
    }

    public List<TaskDTO<Object>> getTasks(CommandsRequesterDTO commandsRequesterDTO) {
        Optional<Planter> planter = findPlanter(commandsRequesterDTO);
        return planter.map(this::getAwaitingTasks).orElseGet(List::of);
    }

    public NextTaskDTO<Object> getNextTask(CommandsRequesterDTO commandsRequesterDTO) {
        Optional<Planter> planter = findPlanter(commandsRequesterDTO);

        if(planter.isEmpty()){
            return new NextTaskDTO<>(0, null);
        }

        int awaitingTasks = planterTaskRepository.countByPlanterAndFinished(planter.get(), false);
        TaskDTO<Object> nextTask = planterTaskRepository.findFirstByPlanterAndFinished(planter.get(), false)
                .map(task -> new TaskDTO<>(task.getTask(), convertJsonToHashMap(task.getParameters())))
                .orElse(null);

        return new NextTaskDTO<>(awaitingTasks, nextTask);
    }

    private Optional<Planter> findPlanter(CommandsRequesterDTO commandsRequesterDTO) {
        return planterRepository
                .findFirstByNameAndMacAddress(commandsRequesterDTO.getName(), commandsRequesterDTO.getMacAddress());
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
}
