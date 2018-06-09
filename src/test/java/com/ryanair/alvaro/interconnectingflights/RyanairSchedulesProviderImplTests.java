package com.ryanair.alvaro.interconnectingflights;

import static org.junit.Assert.assertNotNull;

import java.time.YearMonth;
import java.util.Optional;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import com.ryanair.alvaro.interconnectingflights.logic.route.RouteProvider;
import com.ryanair.alvaro.interconnectingflights.logic.schedules.RyanairScheduleProviderImplService;
import com.ryanair.alvaro.interconnectingflights.model.json.Route;
import com.ryanair.alvaro.interconnectingflights.model.json.ScheduledMonthFlight;

/**
 * Unit test class of RyanairScheduleProviderImpl class.
 * 
 * @author Alvaro Velasco Fernandez
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Import(TestConfig.class)
@TestPropertySource("classpath:global.properties")
public class RyanairSchedulesProviderImplTests {

	@Mock
	private RouteProvider routeProvider;

	@Mock
	private RestTemplate mockTemplate;

	@Autowired
	@InjectMocks
	private RyanairScheduleProviderImplService providerImpl;

	@Autowired
	RestTemplate restTemplate;

	@Test
	public void testRyanairScheduleProvider() {
		ScheduledMonthFlight monthSchedule = getScheduledFlightsDubWroInCurrentMonth();
		assertNotNull(monthSchedule);

		ScheduledMonthFlight.FullScheduledDay daySchedule = monthSchedule.getDays().get(0);

		assertNotNull(daySchedule);
	}

	private final ScheduledMonthFlight getScheduledFlightsDubWroInCurrentMonth() {
		Route route = Route.get("DUB", "WRO");
		YearMonth yearMonth = YearMonth.now();
		return providerImpl.getFlights(route, yearMonth).orElse(null);
	}

	@After
	public void resetProvider() {
		providerImpl.setRestTemplate(restTemplate);
	}

}
