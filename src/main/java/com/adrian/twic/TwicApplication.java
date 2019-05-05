package com.adrian.twic;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class TwicApplication {

	public static void main(String[] args) {
		SpringApplication.run(TwicApplication.class, args);
	}
}