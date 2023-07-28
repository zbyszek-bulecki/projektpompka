package com.sharks.gardenManager.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.UUID;

import static com.sharks.gardenManager.entities.User.TABLE_NAME;

@Entity
@Table(name = TABLE_NAME)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {
    public static final String TABLE_NAME = "Users";
    public static final String COLUMN_PREFIX = "user_";

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(name = COLUMN_PREFIX + "id")
    private UUID id;

    @Column(name = COLUMN_PREFIX + "username")
    private String username;

    @Column(name = COLUMN_PREFIX + "password")
    private String password;

    @Column(name = COLUMN_PREFIX + "role")
    @Enumerated(EnumType.STRING)
    private Role role;

    public enum Role {
        ADMIN, DEVICE
    }
}
