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
    @Column(name = COLUMN_PREFIX + "key")
    private String key;
    @Column(name = COLUMN_PREFIX + "value")
    private String value;
    @Column(name = COLUMN_PREFIX + "update_timestamp")
    private Instant updateTimestamp;

    @JsonIgnoreProperties("planterSettings")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @ManyToOne(targetEntity = Planter.class, fetch = FetchType.LAZY)
    @JoinColumn(name=COLUMN_PREFIX + Planter.COLUMN_PREFIX + "id", nullable=false)
    private Planter planter;

    public boolean isNotDefault() {
        return planter != null;
    }
}
