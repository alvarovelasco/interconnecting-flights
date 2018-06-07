package com.ryanair.alvaro.interconnectingflights;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.time.LocalTime;
import java.time.YearMonth;
import java.util.List;
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

import com.ryanair.alvaro.interconnectingflights.logic.RouteProvider;
import com.ryanair.alvaro.interconnectingflights.logic.RyanairScheduleProviderImpl;
import com.ryanair.alvaro.interconnectingflights.model.Route;
import com.ryanair.alvaro.interconnectingflights.model.ScheduledDayFlight;
import com.ryanair.alvaro.interconnectingflights.model.ScheduledMonthFlight;

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
			ScheduledMonthFlight monthSchedule = getScheduledFlightsDubWroIn62018();
			assertNotNull(monthSchedule);
			
			ScheduledMonthFlight.FullScheduledDay daySchedule = monthSchedule.getDays().get(0);
			
			assertNotNull(daySchedule);
			assertTrue(daySchedule.getFlights().size() == 1);
			assertEquals(7, daySchedule.getDay());
			
			ScheduledDayFlight dayFlight = daySchedule.getFlights().get(0);
			assertEquals(LocalTime.of(19, 30), dayFlight.getDepartureTime());
			assertEquals(LocalTime.of(23, 05), dayFlight.getArrivalTime());
		}

		@Test(expected = RestClientException.class)
		public void testRyanairScheduleProviderFailure() {
			ResponseEntity mockResponseEntity = mock(ResponseEntity.class);
			when(mockResponseEntity.getStatusCode()).thenReturn(HttpStatus.NOT_FOUND);
			when(mockTemplate.getForEntity(anyString(), eq(ScheduledMonthFlight.class), any(Map.class))).thenReturn(mockResponseEntity);

			providerImpl.setRestTemplate(mockTemplate);
			
			getScheduledFlightsDubWroIn62018();
		}
		
		private final ScheduledMonthFlight getScheduledFlightsDubWroIn62018() {
			Route route = Route.get("DUB", "WRO", Optional.empty());
			YearMonth yearMonth = YearMonth.of(2018, 6);
			return providerImpl.getFlights(route, yearMonth);
		}

		@After
		public void resetProvider() {
			providerImpl.setRestTemplate(restTemplate);
		}


}
