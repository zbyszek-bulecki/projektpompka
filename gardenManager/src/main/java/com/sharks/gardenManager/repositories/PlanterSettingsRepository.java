package com.sharks.gardenManager.repositories;

import com.sharks.gardenManager.entities.Planter;
import com.sharks.gardenManager.entities.PlanterSettings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Repository
public interface PlanterSettingsRepository extends JpaRepository<PlanterSettings, UUID> {
    int countByPlanterAndUpdateTimestampGreaterThanEqual(Planter planter, Instant timestamp);
    List<PlanterSettings> findByPlanterAndUpdateTimestampGreaterThanEqual(Planter planter, Instant timestamp);

    @Query("SELECT s FROM PlanterSettings s WHERE (s.planter = :planter OR s.planter IS NULL) AND s.updateTimestamp >= :timestamp")
    List<PlanterSettings> findByPlanterAndUpdateTimestampIncludingDefaultSettings(Planter planter, Instant timestamp);

    @Query("SELECT s FROM PlanterSettings s WHERE s.planter = :planter OR s.planter IS NULL")
    List<PlanterSettings> findByPlanterIncludingDefaultSettings(Planter planter);

    List<PlanterSettings> findByPlanter(Planter planter);
}