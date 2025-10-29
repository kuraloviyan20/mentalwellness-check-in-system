package com.mentalwellness.MentalWellness;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MentalWellnessApplication {

	public static void main(String[] args) {
		SpringApplication.run(MentalWellnessApplication.class, args);
	}

}
