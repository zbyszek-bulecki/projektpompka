package com.sharks.gardenManager.configuration;

import org.mariadb.jdbc.MariaDbDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.sql.SQLException;

@Configuration
public class DataSourceConfig {
    @Value("${spring.datasource.url}")	private String url;
    @Value("${spring.datasource.username}") private String user;
    @Value("${spring.datasource.password}") private String password;

    @Bean
    public DataSource migrate() throws SQLException {
        if (url == null || user == null || password == null) throw new RuntimeException("Missing database credentials");
        return new MariaDbDataSource(url + "?user=" + user + "&password=" + password);
    }
}
