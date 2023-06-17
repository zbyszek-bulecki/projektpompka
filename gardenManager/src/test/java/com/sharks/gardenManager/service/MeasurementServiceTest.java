package com.sharks.gardenManager.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sharks.gardenManager.DTO.MeasurementsReportDTO;
import com.sharks.gardenManager.TestContainersBase;
import com.sharks.gardenManager.entities.Planter;
import com.sharks.gardenManager.entities.PlanterMeasurement;
import com.sharks.gardenManager.repositories.PlanterMeasurementRepository;
import com.sharks.gardenManager.repositories.PlanterRepository;
import jakarta.transaction.Transactional;
import org.assertj.core.api.Assertions;
import org.assertj.core.groups.Tuple;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@SpringBootTest
@Transactional
class MeasurementServiceTest extends TestContainersBase{

    @Autowired
    private PlanterMeasurementRepository planterMeasurementRepository;
    @Autowired
    private PlanterRepository planterRepository;
    @Autowired
    private ObjectMapper objectMapper;

    private MeasurementService objectUnderTest;

    @BeforeEach
    void setUp() {
        planterRepository.deleteAll();
        planterMeasurementRepository.deleteAll();
        objectUnderTest = new MeasurementService(planterMeasurementRepository, planterRepository, objectMapper);
    }

    @Test
    void testMeasurementsService() {
        //Given
        PlanterTestInstance planterTestInstance1 = new PlanterTestInstance("planter_1", "00:00:00:00:00:01");
        PlanterTestInstance planterTestInstance2 = new PlanterTestInstance("planter_2", "00:00:00:00:00:02");

        MeasurementsReportDTO measurementsReportDTO1 = createMeasurement(planterTestInstance1);
        MeasurementsReportDTO measurementsReportDTO2 = createMeasurement(planterTestInstance2);
        MeasurementsReportDTO measurementsReportDTO3 = createMeasurement(planterTestInstance1);

        //When
        objectUnderTest.registerMeasurements(measurementsReportDTO1);
        objectUnderTest.registerMeasurements(measurementsReportDTO2);
        objectUnderTest.registerMeasurements(measurementsReportDTO3);

        //Then
        List<Planter> planterList = planterRepository.findAll();
        Assertions.assertThat(planterList.size()).isEqualTo(2);
        Assertions.assertThat(planterList)
                .extracting(planter -> Tuple.tuple(planter.getName(), planter.getMacAddress()))
                .containsExactlyInAnyOrder(
                        Tuple.tuple(planterTestInstance1.name(), planterTestInstance1.macAddress()),
                        Tuple.tuple(planterTestInstance2.name(), planterTestInstance2.macAddress()));

        List<PlanterMeasurement> planterMeasurementList = planterMeasurementRepository.findAll();
        Assertions.assertThat(planterMeasurementList.size()).isEqualTo(3);
        Assertions.assertThat(planterMeasurementList)
                .extracting(MeasurementServiceTest::getExtractedTuple)
                .containsExactlyInAnyOrder(getMeasurementsReportDTOTuple(measurementsReportDTO1),
                        getMeasurementsReportDTOTuple(measurementsReportDTO2),
                        getMeasurementsReportDTOTuple(measurementsReportDTO3));
    }

    @NotNull
    private static Tuple getMeasurementsReportDTOTuple(MeasurementsReportDTO measurementsReportDTO) {
        return Tuple.tuple(measurementsReportDTO.getName(),
                measurementsReportDTO.getMacAddress(),
                measurementsReportDTO.getTemperature(),
                measurementsReportDTO.getSoilMoisture(),
                measurementsReportDTO.getLightIntensity(),
                measurementsReportDTO.getPressure(),
                measurementsReportDTO.getWaterLevel());
    }

    @NotNull
    private static Tuple getExtractedTuple(PlanterMeasurement planterMeasurement) {
        return Tuple.tuple(planterMeasurement.getPlanter().getName(),
                planterMeasurement.getPlanter().getMacAddress(),
                planterMeasurement.getTemperature(),
                planterMeasurement.getSoilMoisture(),
                planterMeasurement.getLightIntensity(),
                planterMeasurement.getPressure(),
                planterMeasurement.getWaterLevel());
    }

    private MeasurementsReportDTO createMeasurement(PlanterTestInstance planter){
        MeasurementsReportDTO measurementsReportDTO = new MeasurementsReportDTO();
        measurementsReportDTO.setName(planter.name());
        measurementsReportDTO.setMacAddress(planter.macAddress());
        measurementsReportDTO.setTemperature(ThreadLocalRandom.current().nextDouble(10));
        measurementsReportDTO.setSoilMoisture(ThreadLocalRandom.current().nextDouble(10));
        measurementsReportDTO.setLightIntensity(ThreadLocalRandom.current().nextDouble(10));
        measurementsReportDTO.setPressure(ThreadLocalRandom.current().nextDouble(10));
        measurementsReportDTO.setWaterLevel(ThreadLocalRandom.current().nextDouble(10));
        return measurementsReportDTO;
    }

    private record PlanterTestInstance(String name, String macAddress){}

}