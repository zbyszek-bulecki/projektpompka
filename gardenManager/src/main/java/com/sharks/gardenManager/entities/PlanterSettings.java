package com.sharks.gardenManager.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "Planter_Settings")
@Data
public class PlanterSettings {
    @Id
    @Column(name = "id")
    private Long id;
    @Column(name = "setting_key")
    private String settingKey;
    @Column(name = "setting_value")
    private String settingValue;
    @Column(name = "temperature")
    private boolean temperature;
    @ManyToOne
    @JoinColumn(name="planter_id", nullable=false)
    private Planter planter;
}
