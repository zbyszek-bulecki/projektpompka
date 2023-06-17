package com.example.planterSimulator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

@SpringBootApplication
public class PlanterSimulatorApplication {
	public static void main(String[] args) {
		SpringApplication.run(PlanterSimulatorApplication.class, args);
	}

	@EventListener(ApplicationReadyEvent.class)
	public void runThreads() {
		for(int i=0; i<3; i++){
			new SimulationThread("planter-"+i, "00:00:00:00:00:0"+i).start();
		}
	}
}
