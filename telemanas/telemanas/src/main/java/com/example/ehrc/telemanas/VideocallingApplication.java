package com.example.ehrc.telemanas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class VideocallingApplication {

	public static void main(String[] args) {
		SpringApplication.run(VideocallingApplication.class, args);
	}

}
