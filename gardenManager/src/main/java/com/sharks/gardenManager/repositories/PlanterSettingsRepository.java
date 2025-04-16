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
            SELECT new com.sharks.gardenManager.entities.PlanterSettingsWithDefaults(d.id, d.key, s.value, d.value, s.updateTimestamp, s.planter)
                        FROM PlanterSettings d
                        LEFT JOIN PlanterSettings s
                            ON s.planter = :planter
                            AND s.key=d.key
                        WHERE d.planter IS NULL AND (d.updateTimestamp >= :timestamp OR s.updateTimestamp >= :timestamp)
           """)
    List<PlanterSettingsWithDefaults> findByPlanterAndUpdateTimestampIncludingDefaultSettings(Planter planter, Instant timestamp);

    @Query("""
            SELECT new com.sharks.gardenManager.entities.PlanterSettingsWithDefaults(d.id, d.key, s.value, d.value, s.updateTimestamp, s.planter)
                        FROM PlanterSettings d
                        LEFT JOIN PlanterSettings s
                            ON s.planter = :planter
                            AND s.key=d.key
                        WHERE d.planter IS NULL
            """)
    List<PlanterSettingsWithDefaults> findByPlanterIncludingDefaultSettings(Planter planter);

    List<PlanterSettings> findByPlanter(Planter planter);
}