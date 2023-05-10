package com.sharks.gardenManager.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;


@Entity
@Table(name = "Planters")
@Data
public class Planter {
    @Id
    @Column(name = "id")
    private Long id;
    @Column(name = "name")
    private String name;
    @Column(name = "mac_address")
    private String macAddress;
    @Column(name = "last_activity")
    private LocalDateTime lastActivity;
    @OneToMany(mappedBy="planter", fetch = FetchType.LAZY)
    private List<PlanterMeasurement> planterMeasurement;
    @OneToMany(mappedBy="planter", fetch = FetchType.LAZY)
    private List<PlanterMeasurement> planterSettings;
    @OneToMany(mappedBy="planter", fetch = FetchType.LAZY)
    private List<PlanterMeasurement> planterTask;
}
