package com.sharks.gardenManager.tasks;

import com.sharks.gardenManager.entities.*;
import com.sharks.gardenManager.repositories.PlanterMeasurementRepository;
import com.sharks.gardenManager.repositories.PlanterRepository;
import com.sharks.gardenManager.repositories.PlanterTaskRepository;
import com.sharks.gardenManager.service.SettingsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class PlanterTaskManager {


    private final PlanterRepository planterRepository;
    private final PlanterMeasurementRepository planterMeasurementRepository;
    private final PlanterTaskRepository planterTaskRepository;
    private final SettingsService settingsService;
    private final List<TaskLogic> taskLogicList;

    public PlanterTaskManager(PlanterRepository planterRepository,
                              PlanterMeasurementRepository planterMeasurementRepository,
                              PlanterTaskRepository planterTaskRepository,
                              SettingsService settingsService,
                              List<TaskLogic> taskLogicList) {
        this.planterRepository = planterRepository;
        this.planterMeasurementRepository = planterMeasurementRepository;
        this.planterTaskRepository = planterTaskRepository;
        this.settingsService = settingsService;
        this.taskLogicList = taskLogicList;
    }

    @Scheduled(fixedRate = 600000)
    void run() {
        log.info("Test of PlanterTaskManager");
        List<Planter> planterList = planterRepository.findAll();

        for (Planter planter : planterList) {
            processPlanterTasks(planter);
        }
    }

    void processPlanterTasks(Planter planter) {
        Map<String, PlanterSettingsWithDefaults> settingsMap = settingsService.getSettingsFromDBIncludingDefaultsAndRemoveDuplicates(planter, null);
        PlanterMeasurement planterMeasurement = planterMeasurementRepository.findLastMeasurement(planter);
        PlanterStatusAndSettings planterStatusAndSettings = PlanterStatusAndSettings.of(planter, planterMeasurement, settingsMap);
        List<PlanterTask> planterTasks = planterTaskRepository.findByPlanterAndFinished(planter, false);
        for (TaskLogic taskLogic : taskLogicList) {
            executeTask(taskLogic, planterStatusAndSettings, planterTasks);
        }
    }

    void executeTask(TaskLogic taskLogic, PlanterStatusAndSettings planterStatusAndSettings, List<PlanterTask> planterTasks) {
        PlanterTask planterTask = taskLogic.runLogic(planterStatusAndSettings, planterTasks);
        if (planterTask != null) planterTaskRepository.save(planterTask);
    }
}
