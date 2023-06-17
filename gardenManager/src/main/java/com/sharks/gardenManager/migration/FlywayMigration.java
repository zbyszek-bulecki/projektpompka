package com.sharks.gardenManager.migration;

import org.flywaydb.core.Flyway;
import org.mariadb.jdbc.MariaDbDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;

import javax.sql.DataSource;
import java.sql.SQLException;


public class FlywayMigration implements ApplicationListener<ApplicationReadyEvent> {

    @Value("${spring.datasource.url}")	private String url;
    @Value("${spring.datasource.username}") private String user;
    @Value("${spring.datasource.password}") private String password;

    public void migrate() throws SQLException {
        if(url==null || user==null || password==null) throw new RuntimeException("Missing database credentials");
        DataSource dataSource = new MariaDbDataSource(url+"?user="+user+"&password="+password);
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
