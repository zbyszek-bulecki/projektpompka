package com.sharks.gardenManager.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.format.annotation.DateTimeFormat;


import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


@Entity
@Table(name = Planter.TABLE_NAME)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Planter {
    public static final String TABLE_NAME = "Planters";
    public static final String COLUMN_PREFIX = "planter_";

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(name = COLUMN_PREFIX + "id")
    private UUID id;
    @Column(name = COLUMN_PREFIX + "name")
    private String name;
    @Column(name = COLUMN_PREFIX + "mac_address")
    private String macAddress;
    @Column(name = COLUMN_PREFIX + "last_activity")
    private Instant lastActivity;
    @JsonIgnoreProperties("planter")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @OneToMany(mappedBy="planter", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<PlanterMeasurement> planterMeasurement;
    @JsonIgnoreProperties("planter")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @OneToMany(mappedBy="planter", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<PlanterSettings> planterSettings;
    @JsonIgnoreProperties("planter")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @OneToMany(mappedBy="planter", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<PlanterTask> planterTasks;
}
