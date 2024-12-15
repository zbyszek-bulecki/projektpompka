package com.sharks.gardenManager.tasks;

import com.sharks.gardenManager.entities.Planter;
import com.sharks.gardenManager.entities.PlanterMeasurement;
import com.sharks.gardenManager.entities.PlanterSettings;

import java.util.Map;

public record PlanterStatusAndSettings(Planter planter, PlanterMeasurement measurement, Map<String, PlanterSettings> settings) {
    public static PlanterStatusAndSettings of(Planter planter, PlanterMeasurement measurement, Map<String, PlanterSettings> settings) {
        return new PlanterStatusAndSettings(planter, measurement, settings);
    }
}
