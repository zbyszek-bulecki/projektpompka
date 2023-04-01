package com.example.databaseMigration;

import org.flywaydb.core.Flyway;
import org.mariadb.jdbc.MariaDbDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import javax.sql.DataSource;
import java.sql.SQLException;

@SpringBootApplication(exclude={DataSourceAutoConfiguration.class})
public class DatabaseMigrationApplication {
	@Value("${flyway.url}")	private String url;
	@Value("${flyway.user}") private String user;
	@Value("${flyway.password}") private String password;


	public static void main(String[] args) {
		SpringApplication.run(DatabaseMigrationApplication.class, args);
	}

	@EventListener(ApplicationReadyEvent.class)
	public void startMigration() throws SQLException {
		DataSource dataSource = new MariaDbDataSource(url+"?user="+user+"&password="+password);
		Flyway flyway = Flyway.configure()
				.dataSource(dataSource)
				.locations("db/migration")
				.load();
		flyway.migrate();
	}


}
