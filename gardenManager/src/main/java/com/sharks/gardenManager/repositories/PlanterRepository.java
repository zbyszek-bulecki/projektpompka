package com.sharks.gardenManager.repositories;

import com.sharks.gardenManager.entities.Planter;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PlanterRepository extends JpaRepository<Planter, UUID> {

    Page<Planter> findAll(Pageable pageable);
    Optional<Planter> findByNameAndMacAddress(String name, String macAddress);

}
