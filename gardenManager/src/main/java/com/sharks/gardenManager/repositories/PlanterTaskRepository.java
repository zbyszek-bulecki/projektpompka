package com.sharks.gardenManager.repositories;

import com.sharks.gardenManager.entities.Planter;
import com.sharks.gardenManager.entities.PlanterTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PlanterTaskRepository extends JpaRepository<PlanterTask, UUID> {
    int countByPlanterAndFinished(Planter planter, boolean finished);
    List<PlanterTask> findByPlanterAndFinished(Planter planter, boolean finished);
    Optional<PlanterTask> findFirstByPlanterAndFinished(Planter planter, boolean finished);
}
