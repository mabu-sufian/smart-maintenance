package com.example.maintenance;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SmartMaintenanceApplication {

	public static void main(String[] args) {

		System.out.println("Shawon");
		SpringApplication.run(SmartMaintenanceApplication.class, args);
	}

}
