package com.sharks.gardenManager.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sharks.gardenManager.DTO.CommandsRequestDTO;
import com.sharks.gardenManager.DTO.NextTaskDTO;
import com.sharks.gardenManager.DTO.TaskConfirmationRequestDTO;
import com.sharks.gardenManager.DTO.TaskDTO;
import com.sharks.gardenManager.entities.Planter;
import com.sharks.gardenManager.entities.PlanterTask;
import com.sharks.gardenManager.repositories.PlanterRepository;
import com.sharks.gardenManager.repositories.PlanterTaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    public List<TaskDTO<Object>> getTasks(CommandsRequestDTO commandsRequestDTO) {
        Optional<Planter> planter = findPlanter(commandsRequestDTO.getName(), commandsRequestDTO.getMacAddress());
        return planter.map(this::getAwaitingTasks).orElseGet(List::of);
    }

    public NextTaskDTO<Object> getNextTask(CommandsRequestDTO commandsRequestDTO) {
        Optional<Planter> planter = findPlanter(commandsRequestDTO.getName(), commandsRequestDTO.getMacAddress());

        if(planter.isEmpty()){
            return new NextTaskDTO<>(0, null);
        }

        int awaitingTasks = planterTaskRepository.countByPlanterAndFinished(planter.get(), false);
        TaskDTO<Object> nextTask = planterTaskRepository.findFirstByPlanterAndFinished(planter.get(), false)
                .map(task -> new TaskDTO<>(task.getTask(), convertJsonToHashMap(task.getParameters())))
                .orElse(null);

        return new NextTaskDTO<>(awaitingTasks, nextTask);
    }

    public boolean confirmTask (TaskConfirmationRequestDTO taskConfirmationRequestDTO) {
        Optional<Planter> planter = findPlanter(taskConfirmationRequestDTO.getName(), taskConfirmationRequestDTO.getMacAddress());
        if(planter.isEmpty()){
            return false;
        }
        List<PlanterTask> planterTasks = planterTaskRepository.findByPlanterAndTaskAndFinished(planter.get(), taskConfirmationRequestDTO.getCommand(), false);
        if (planterTasks.isEmpty()) {
            return false;
        }
        planterTasks.forEach(task -> {
            task.setFinished(true);
            planterTaskRepository.save(task);
        });
        return true;
    }

    private Optional<Planter> findPlanter(String name, String macAddress) {
        return planterRepository
                .findFirstByNameAndMacAddress(name, macAddress);
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
