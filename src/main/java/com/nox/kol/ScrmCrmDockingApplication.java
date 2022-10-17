package com.nox.kol;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ScrmCrmDockingApplication {

	public static void main(String[] args) {
		SpringApplication.run(ScrmCrmDockingApplication.class, args);
	}

}
