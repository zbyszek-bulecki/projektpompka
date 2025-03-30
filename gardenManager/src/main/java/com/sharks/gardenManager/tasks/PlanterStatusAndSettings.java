package com.sharks.gardenManager.tasks;

import com.sharks.gardenManager.entities.Planter;
import com.sharks.gardenManager.entities.PlanterMeasurement;
import com.sharks.gardenManager.entities.PlanterSettingsWithDefaults;

import java.util.Map;

public record PlanterStatusAndSettings(Planter planter, PlanterMeasurement measurement, Map<String, PlanterSettingsWithDefaults> settings) {
    public static PlanterStatusAndSettings of(Planter planter, PlanterMeasurement measurement, Map<String, PlanterSettingsWithDefaults> settings) {
        return new PlanterStatusAndSettings(planter, measurement, settings);
    }
}
