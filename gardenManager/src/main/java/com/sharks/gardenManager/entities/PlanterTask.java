package com.sharks.gardenManager.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.UUID;

import static com.sharks.gardenManager.entities.PlanterTask.TABLE_NAME;

@Entity
@Table(name = TABLE_NAME)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PlanterTask {
    public static final String TABLE_NAME = "Planter_Tasks";
    public static final String COLUMN_PREFIX = "task_";

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(name = COLUMN_PREFIX + "id")
    private UUID id;
    @Column(name = COLUMN_PREFIX + "task")
    private String task;
    @Column(name = COLUMN_PREFIX + "parameters")
    private String parameters;
    @Column(name = COLUMN_PREFIX + "finished")
    private boolean finished;

    @JsonIgnoreProperties("planterTasks")
    @ManyToOne(targetEntity = Planter.class, fetch = FetchType.LAZY)
    @JoinColumn(name= COLUMN_PREFIX + Planter.COLUMN_PREFIX + "id", nullable=false)
    private Planter planter;
}
