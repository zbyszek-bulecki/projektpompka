package com.sharks.gardenManager.repositories;

import com.sharks.gardenManager.entities.Planter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PlanterRepository extends JpaRepository<Planter, UUID> {
    Optional<Planter> findByNameAndMacAddress(String name, String macAddress);

}
