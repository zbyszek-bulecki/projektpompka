package com.sharks.gardenManager.repositories;

import com.sharks.gardenManager.entities.Planter;
import com.sharks.gardenManager.entities.PlanterSettings;
import com.sharks.gardenManager.entities.PlanterTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PlanterSettingsRepository extends JpaRepository<PlanterSettings, UUID> {
    int countByPlanterAndUpdated(Planter planter, boolean updated);
    List<PlanterSettings> findByPlanterAndUpdated(Planter planter, boolean updated);
}