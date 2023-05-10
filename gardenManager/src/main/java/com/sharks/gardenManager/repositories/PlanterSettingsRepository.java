package com.sharks.gardenManager.repositories;

import com.sharks.gardenManager.entities.PlanterSettings;
import com.sharks.gardenManager.entities.PlanterTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlanterSettingsRepository extends JpaRepository<PlanterSettings, Long> {
}