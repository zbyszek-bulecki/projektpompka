package com.sharks.gardenManager;

import com.sharks.gardenManager.migration.FlywayMigration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MariaDBContainer;

public class TestContainersBase {

    private static final MariaDBContainer<?> mariaDBContainer = new MariaDBContainer<>("mariadb:10.5.8")
            .withCommand("--default-authentication-plugin=mysql_native_password", "--lower_case_table_names=1");

    @DynamicPropertySource
    static void registerProperties(DynamicPropertyRegistry registry) {
        mariaDBContainer.start();
        registry.add("spring.datasource.url", mariaDBContainer::getJdbcUrl);
        registry.add("spring.datasource.username", mariaDBContainer::getUsername);
        registry.add("spring.datasource.password", mariaDBContainer::getPassword);
    }
}
