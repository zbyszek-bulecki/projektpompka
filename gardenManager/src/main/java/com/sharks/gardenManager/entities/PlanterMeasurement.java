package com.sharks.gardenManager.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "Planter_Measurement")
@Data
public class PlanterMeasurement {
    @Id
    @Column(name = "id")
    private Long id;
    @Column(name = "soil_moisture")
    private double soilMoisture;
    @Column(name = "light_intensity")
    private double lightIntensity;
    @Column(name = "temperature")
    private double temperature;
    @Column(name = "pressure")
    private double pressure;
    @ManyToOne
    @JoinColumn(name="planter_id", nullable=false)
    private Planter planter;
}
