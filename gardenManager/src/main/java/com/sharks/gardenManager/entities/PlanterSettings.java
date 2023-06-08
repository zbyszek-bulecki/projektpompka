package com.sharks.gardenManager.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.UUID;

import static com.sharks.gardenManager.entities.PlanterSettings.TABLE_NAME;

@Entity
@Table(name = TABLE_NAME)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PlanterSettings {
    public static final String TABLE_NAME = "Planter_Settings";
    public static final String COLUMN_PREFIX = "settings_";

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(name = COLUMN_PREFIX + "id")
    private UUID id;
    @Column(name = COLUMN_PREFIX + "setting_key")
    private String settingKey;
    @Column(name = COLUMN_PREFIX + "setting_value")
    private String settingValue;
    @Column(name = COLUMN_PREFIX + "updated")
    private boolean updated;

    @JsonIgnoreProperties("planterSettings")
    @ManyToOne(targetEntity = Planter.class, fetch = FetchType.LAZY)
    @JoinColumn(name=COLUMN_PREFIX + Planter.COLUMN_PREFIX + "id", nullable=false)
    private Planter planter;
}
