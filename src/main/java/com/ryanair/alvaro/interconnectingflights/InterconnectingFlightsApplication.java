package com.ryanair.alvaro.interconnectingflights;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages="com.ryanair.alvaro.interconnectingflights.logic")
public class InterconnectingFlightsApplication {

	public static void main(String[] args) {
		SpringApplication.run(InterconnectingFlightsApplication.class, args);
	}
}
