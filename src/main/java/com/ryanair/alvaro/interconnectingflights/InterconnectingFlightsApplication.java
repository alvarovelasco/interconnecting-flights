package com.ryanair.alvaro.interconnectingflights;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@Import(InterconnectingFlightsApplicationConfig.class)
@ComponentScan(basePackages = "com.ryanair.alvaro.interconnectingflights")
@PropertySource("classpath:global.properties")
public class InterconnectingFlightsApplication extends SpringBootServletInitializer{

	public static void main(String[] args) {
		SpringApplication.run(InterconnectingFlightsApplication.class, args);
	}
	
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(InterconnectingFlightsApplication.class);
	}

//	@Component
//	public class CommandLineAppStartupRunner implements CommandLineRunner {
//		@Autowired
//		private RyanairRouteProviderImplService routeProviderImpl;
//
//		@Override
//		public void run(String... args) throws Exception {
//			System.out.println(routeProviderImpl.getRoutes());
//
//		}
//	}
}
