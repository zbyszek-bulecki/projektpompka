package com.sharks.gardenManager.tasks;

import com.sharks.gardenManager.entities.PlanterTask;

import java.util.List;

public interface TaskLogic {

    PlanterTask runLogic(PlanterStatusAndSettings planterStatusAndSettings, List<PlanterTask> planterTasks);

}
