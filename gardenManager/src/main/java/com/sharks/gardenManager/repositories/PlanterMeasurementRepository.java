package com.sharks.gardenManager.repositories;

import com.sharks.gardenManager.entities.PlanterMeasurement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlanterMeasurementRepository extends JpaRepository<PlanterMeasurement, Long> {
}