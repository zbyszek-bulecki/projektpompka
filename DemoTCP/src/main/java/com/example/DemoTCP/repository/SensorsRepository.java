package com.example.DemoTCP.repository;

import com.example.DemoTCP.entity.Sensors;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SensorsRepository extends JpaRepository<Sensors, Integer> {
    @Query("SELECT s FROM Sensors s")
    List<Sensors> getAllSensors();

    List<Sensors> getSensorsByAddress(String Address);

}
