package com.sharks.gardenManager.repositories;

import com.sharks.gardenManager.entities.Planter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlanterRepository extends JpaRepository<Planter, Long> {
}
