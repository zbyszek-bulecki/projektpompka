package com.sharks.gardenManager.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "Planter_Tasks")
@Data
public class PlanterTask {
    @Id
    @Column(name = "id")
    private Long id;
    @Column(name = "task")
    private String task;
    @Column(name = "parameters")
    private String parameters;
    @ManyToOne
    @JoinColumn(name="planter_id", nullable=false)
    private Planter planter;
}
