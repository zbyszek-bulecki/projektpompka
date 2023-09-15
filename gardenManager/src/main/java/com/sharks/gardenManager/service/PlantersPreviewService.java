package com.sharks.gardenManager.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sharks.gardenManager.DTO.MeasurementsDTO;
import com.sharks.gardenManager.DTO.PlanterDTO;
import com.sharks.gardenManager.DTO.PlanterWithLatestMeasurementDTO;
import com.sharks.gardenManager.entities.Planter;
import com.sharks.gardenManager.entities.PlanterMeasurement;
import com.sharks.gardenManager.repositories.PlanterMeasurementRepository;
import com.sharks.gardenManager.repositories.PlanterRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

@Service
public class PlantersPreviewService {
    private final PlanterRepository planterRepository;
    private final PlanterMeasurementRepository planterMeasurementRepository;
    private final ObjectMapper objectMapper;

    public PlantersPreviewService(PlanterRepository planterRepository, PlanterMeasurementRepository planterMeasurementService, ObjectMapper objectMapper) {
        this.planterRepository = planterRepository;
        this.planterMeasurementRepository = planterMeasurementService;
        this.objectMapper = objectMapper;
    }

    public List<PlanterWithLatestMeasurementDTO> getPlantersList() {
        List<Planter> planters = planterRepository.findAll();
        Map<Planter, PlanterMeasurement> plantersWithLatestMeasurements = getLatestMeasurements(planters);
        List<PlanterWithLatestMeasurementDTO> plantersWithMeasurements = mapToPlanterWithLatestMeasurementDTO(planters, plantersWithLatestMeasurements);
        addPlantersWithoutMeasurements(planters, plantersWithLatestMeasurements, plantersWithMeasurements);
        return plantersWithMeasurements;
    }

    private Map<Planter, PlanterMeasurement> getLatestMeasurements(List<Planter> planters) {
        return this.planterMeasurementRepository
                .findLastMeasurementForEachDevice(planters)
                .stream().collect(groupingBy(PlanterMeasurement::getPlanter))
                .values()
                .stream()
                .map(list -> list.get(0))
                .collect(Collectors.toMap(PlanterMeasurement::getPlanter, planterMeasurement -> planterMeasurement));
    }

    private List<PlanterWithLatestMeasurementDTO> mapToPlanterWithLatestMeasurementDTO(List<Planter> planters, Map<Planter, PlanterMeasurement> plantersWithLatestMeasurements) {
        return planters.stream()
                .map(planter -> PlanterWithLatestMeasurementDTO.mapToDTO(planter, plantersWithLatestMeasurements.get(planter)))
                .collect(Collectors.toList());
    }

    private void addPlantersWithoutMeasurements(List<Planter> planters, Map<Planter, PlanterMeasurement> plantersWithLatestMeasurements, List<PlanterWithLatestMeasurementDTO> plantersWithMeasurements) {
        plantersWithMeasurements.addAll(
                planters.stream()
                        .filter(planter -> !plantersWithLatestMeasurements.containsKey(planter))
                        .map(planter -> objectMapper.convertValue(planter, PlanterWithLatestMeasurementDTO.class)).toList());
    }

    public PlanterDTO getPlanterByNameAndMacAddress(String name, String macAddress) {
        return planterRepository.findByNameAndMacAddress(name, macAddress)
                .map(PlanterDTO::mapToDTO)
                .orElseThrow();
    }

    public List<MeasurementsDTO> getMeasurementsByPlanterNameAndMacAddress(String name, String macAddress, int page, int size) {
        return planterMeasurementRepository.findAllByPlanterNameAndMacAddress(name, macAddress, Pageable.ofSize(size).withPage(page))
                .stream()
                .map(MeasurementsDTO::mapToDTO)
                .collect(Collectors.toList());
    }
}
