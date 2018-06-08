package com.ryanair.alvaro.interconnectingflights;

import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.YearMonth;
import java.util.Map;
import java.util.Optional;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.ryanair.alvaro.interconnectingflights.logic.route.RouteProvider;
import com.ryanair.alvaro.interconnectingflights.logic.scheduler.RyanairScheduleProviderImpl;
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
	private RyanairScheduleProviderImpl providerImpl;

	@Autowired
	RestTemplate restTemplate;

	@Test
	public void testRyanairScheduleProvider() {
		ScheduledMonthFlight monthSchedule = getScheduledFlightsDubWroInCurrentMonth();
		assertNotNull(monthSchedule);

		ScheduledMonthFlight.FullScheduledDay daySchedule = monthSchedule.getDays().get(0);

		assertNotNull(daySchedule);
	}

	@Test(expected = RestClientException.class)
	public void testRyanairScheduleProviderFailure() {
		ResponseEntity mockResponseEntity = mock(ResponseEntity.class);
		when(mockResponseEntity.getStatusCode()).thenReturn(HttpStatus.NOT_FOUND);
		when(mockTemplate.getForEntity(anyString(), eq(ScheduledMonthFlight.class), any(Map.class)))
				.thenReturn(mockResponseEntity);

		providerImpl.setRestTemplate(mockTemplate);

		getScheduledFlightsDubWroInCurrentMonth();
	}

	private final ScheduledMonthFlight getScheduledFlightsDubWroInCurrentMonth() {
		Route route = Route.get("DUB", "WRO", Optional.empty());
		YearMonth yearMonth = YearMonth.now();
		return providerImpl.getFlights(route, yearMonth).orElse(null);
	}

	@After
	public void resetProvider() {
		providerImpl.setRestTemplate(restTemplate);
	}

}
