package com.ryanair.alvaro.interconnectingflights;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import com.ryanair.alvaro.interconnectingflights.logic.route.RyanairRouteProviderImpl;

@SpringBootApplication
@Import(InterconnectingFlightsApplicationConfig.class)
@ComponentScan(basePackages = "com.ryanair.alvaro.interconnectingflights.logic")
@PropertySource("classpath:global.properties")
public class InterconnectingFlightsApplication {

	public static void main(String[] args) {
		SpringApplication.run(InterconnectingFlightsApplication.class, args);
	}

	@Component
	public class CommandLineAppStartupRunner implements CommandLineRunner {
		@Autowired
		private RyanairRouteProviderImpl routeProviderImpl;

		@Override
		public void run(String... args) throws Exception {
			System.out.println(routeProviderImpl.getRoutes());

		}
	}
}
