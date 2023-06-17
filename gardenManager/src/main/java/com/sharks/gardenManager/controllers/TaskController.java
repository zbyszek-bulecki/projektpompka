package com.sharks.gardenManager.controllers;

import com.sharks.gardenManager.DTO.CommandsRequesterDTO;
import com.sharks.gardenManager.DTO.TaskDTO;
import com.sharks.gardenManager.service.TasksService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/tasks")
public class TaskController {

    private final TasksService tasksService;

    public TaskController(TasksService tasksService) {
        this.tasksService = tasksService;
    }

    @PostMapping()
    public List<TaskDTO<Object>> getTasks(@RequestBody CommandsRequesterDTO commandsRequesterDTO){
        return tasksService.getTasks(commandsRequesterDTO);
    }
}
