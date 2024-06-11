package com.sharks.gardenManager.migration;

import org.flywaydb.core.Flyway;
import org.mariadb.jdbc.MariaDbDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;

import javax.sql.DataSource;
import java.sql.SQLException;


public class FlywayMigration implements ApplicationListener<ApplicationReadyEvent> {

    @Autowired
    private DataSource dataSource;

    public void migrate() throws SQLException {
        Flyway flyway = Flyway.configure()
                .dataSource(dataSource)
                .locations("db/migration")
                .load();
        flyway.migrate();
    }

    @Override
    public void onApplicationEvent(final ApplicationReadyEvent event) {
        try {
            migrate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
