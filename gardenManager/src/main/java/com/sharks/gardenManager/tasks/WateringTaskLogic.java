package com.sharks.gardenManager.tasks;

import com.sharks.gardenManager.entities.PlanterSettingsWithDefaults;
import com.sharks.gardenManager.entities.PlanterTask;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class WateringTaskLogic implements TaskLogic {

    private final static String TASK_NAME = "Watering";

    @Override
    public PlanterTask runLogic(PlanterStatusAndSettings planterStatusAndSettings, List<PlanterTask> planterTasks) {
        log.info("Hello from WateringTaskLogic");
        if(planterTasks.stream().map(PlanterTask::getTask).anyMatch(TASK_NAME::equals)) return null;
        PlanterSettingsWithDefaults planterSettingsWithDefaults = planterStatusAndSettings.settings().get("watering_threshold");
        if(planterSettingsWithDefaults == null) {
            log.info("{}",planterStatusAndSettings);
            log.error("SETTINGS NOT FOUND!!");
            return null;
        }
        String wateringThresholdStr = planterSettingsWithDefaults.getValue();
        if (wateringThresholdStr == null || planterStatusAndSettings.measurement() == null) return null;
        Double wateringThreshold = Double.valueOf(wateringThresholdStr);
        Double soilMoisture = planterStatusAndSettings.measurement().getSoilMoisture();
        if (soilMoisture < wateringThreshold ) {
            return new PlanterTask(null, TASK_NAME, "", false, planterStatusAndSettings.planter());
        }
        return null;
    }


}
