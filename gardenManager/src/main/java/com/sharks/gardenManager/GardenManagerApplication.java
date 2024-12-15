package com.sharks.gardenManager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class GardenManagerApplication {
	public static void main(String[] args) {
		SpringApplication.run(GardenManagerApplication.class, args);
	}
}
