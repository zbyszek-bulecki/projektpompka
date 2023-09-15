package com.sharks.gardenManager.repositories;

import com.sharks.gardenManager.entities.Planter;
import com.sharks.gardenManager.entities.PlanterMeasurement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PlanterMeasurementRepository extends JpaRepository<PlanterMeasurement, UUID> {
    @Query(value = "SELECT m FROM PlanterMeasurement m " +
            "LEFT JOIN (SELECT MAX(ma.createdAt) date, ma.planter planter " +
            "FROM PlanterMeasurement ma GROUP BY ma.planter) lm " +
            "WHERE m.createdAt=lm.date AND lm.planter IN :ids")
    List<PlanterMeasurement> findLastMeasurementForEachDevice(@Param("ids") List<Planter> idList);

    @Query(value = "SELECT m FROM PlanterMeasurement m " +
            "WHERE m.planter.name = :name AND m.planter.macAddress = :macAddress " +
            "ORDER BY m.createdAt DESC")
    Page<PlanterMeasurement> findAllByPlanterNameAndMacAddress(String name, String macAddress, Pageable pageable);
}