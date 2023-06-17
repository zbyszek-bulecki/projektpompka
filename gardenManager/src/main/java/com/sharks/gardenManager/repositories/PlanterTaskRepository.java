package com.sharks.gardenManager.repositories;

import com.sharks.gardenManager.entities.Planter;
import com.sharks.gardenManager.entities.PlanterTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PlanterTaskRepository extends JpaRepository<PlanterTask, UUID> {
    Optional<PlanterTask> findByPlanterAndFinished(Planter planter, boolean finished);
}
