package com.sharks.gardenManager.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

import static com.sharks.gardenManager.entities.PlanterMeasurement.TABLE_NAME;

@Entity
@Table(name = TABLE_NAME)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class PlanterMeasurement {
    public static final String TABLE_NAME = "Planter_Measurement";
    public static final String COLUMN_PREFIX = "measurement_";

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(name = COLUMN_PREFIX + "id")
    private UUID id;
    @Column(name = COLUMN_PREFIX + "soil_moisture")
    private double soilMoisture;
    @Column(name = COLUMN_PREFIX + "light_intensity")
    private double lightIntensity;
    @Column(name = COLUMN_PREFIX + "temperature")
    private double temperature;
    @Column(name = COLUMN_PREFIX + "pressure")
    private double pressure;
    @Column(name = COLUMN_PREFIX + "water_level")
    private double waterLevel;
    @Column(name = COLUMN_PREFIX + "created_at")
    private Instant createdAt;

    @JsonIgnoreProperties("planterMeasurement")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name= COLUMN_PREFIX + Planter.COLUMN_PREFIX + "id", nullable=false)
    private Planter planter;
}
