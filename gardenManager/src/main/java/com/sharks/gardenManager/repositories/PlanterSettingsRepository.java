package com.sharks.gardenManager.repositories;

import com.sharks.gardenManager.entities.Planter;
import com.sharks.gardenManager.entities.PlanterSettings;
import com.sharks.gardenManager.entities.PlanterSettingsWithDefaults;
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

    @Query("""
            SELECT new com.sharks.gardenManager.entities.PlanterSettingsWithDefaults(s.id, s.key, s.value, d.value, s.updateTimestamp, s.planter)
            FROM PlanterSettings d
            JOIN PlanterSettings s ON s.planter = :planter AND s.updateTimestamp >= :timestamp AND s.key=d.key
            WHERE d.planter IS NULL""")
    List<PlanterSettingsWithDefaults> findByPlanterAndUpdateTimestampIncludingDefaultSettings(Planter planter, Instant timestamp);

    @Query("""
            SELECT new com.sharks.gardenManager.entities.PlanterSettingsWithDefaults(s.id, s.key, s.value, d.value, s.updateTimestamp, s.planter)
            FROM PlanterSettings d
            JOIN PlanterSettings s ON s.planter = :planter  AND s.key=d.key
            WHERE d.planter IS NULL
            """)
    List<PlanterSettingsWithDefaults> findByPlanterIncludingDefaultSettings(Planter planter);

    List<PlanterSettings> findByPlanter(Planter planter);
}