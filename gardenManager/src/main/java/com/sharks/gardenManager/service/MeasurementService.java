package com.sharks.gardenManager.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sharks.gardenManager.DTO.MeasurementsReportDTO;
import com.sharks.gardenManager.entities.Planter;
import com.sharks.gardenManager.entities.PlanterMeasurement;
import com.sharks.gardenManager.repositories.PlanterMeasurementRepository;
import com.sharks.gardenManager.repositories.PlanterRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class MeasurementService {
    private final PlanterMeasurementRepository planterMeasurementRepository;
    private final PlanterRepository planterRepository;
    private final ObjectMapper objectMapper;

    public MeasurementService(PlanterMeasurementRepository planterMeasurementRepository, PlanterRepository planterRepository, ObjectMapper objectMapper) {
        this.planterMeasurementRepository = planterMeasurementRepository;
        this.planterRepository = planterRepository;
        this.objectMapper = objectMapper;
    }

    @Transactional
    public void registerMeasurements(MeasurementsReportDTO measurementsDTO){
        Optional<Planter> optionalPlanter = planterRepository
                .findFirstByNameAndMacAddress(measurementsDTO.getName(), measurementsDTO.getMacAddress());
        if(optionalPlanter.isPresent()){
            saveMeasurementsForExistingPlanter(measurementsDTO, optionalPlanter.get());
        }
        else {
            saveMeasurementsForNewPlanter(measurementsDTO);
        }

    }

    private void saveMeasurementsForExistingPlanter(MeasurementsReportDTO measurementsDTO, Planter planter) {
        Instant now = Instant.now();
        planter.setLastActivity(now);

        PlanterMeasurement planterMeasurement = objectMapper.convertValue(measurementsDTO, PlanterMeasurement.class);
        planterMeasurement.setPlanter(planter);
        planterMeasurement.setCreatedAt(now);
        planterMeasurementRepository.save(planterMeasurement);
    }

    private void saveMeasurementsForNewPlanter(MeasurementsReportDTO measurementsDTO){
        Instant now = Instant.now();

        Planter planter = new Planter();
        planter.setName(measurementsDTO.getName());
        planter.setMacAddress(measurementsDTO.getMacAddress());
        planter.setLastActivity(now);

        PlanterMeasurement planterMeasurement = objectMapper.convertValue(measurementsDTO, PlanterMeasurement.class);
        planterMeasurement.setPlanter(planter);
        planterMeasurement.setCreatedAt(now);

        planterMeasurementRepository.save(planterMeasurement);
    }
}
