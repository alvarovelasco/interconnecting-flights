package com.ryanair.alvaro.interconnectingflights;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.ryanair.alvaro.interconnectingflights.logic.ScheduleAbsoluteRouteInterconnectionResolverImplService;
import com.ryanair.alvaro.interconnectingflights.logic.route.RouteResolver;
import com.ryanair.alvaro.interconnectingflights.logic.schedules.ScheduleProvider;
import com.ryanair.alvaro.interconnectingflights.logic.schedules.ScheduledDateFlight;
import com.ryanair.alvaro.interconnectingflights.model.ResolvedRoute;
import com.ryanair.alvaro.interconnectingflights.model.json.ResolvedSchedule;
import com.ryanair.alvaro.interconnectingflights.model.json.Route;
import com.ryanair.alvaro.interconnectingflights.model.json.ScheduledDayFlight;
import com.ryanair.alvaro.interconnectingflights.model.json.ScheduledMonthFlight;
import com.ryanair.alvaro.interconnectingflights.model.json.ScheduledMonthFlight.FullScheduledDay;

@RunWith(SpringRunner.class)
@SpringBootTest
@Import(TestConfig.class)
@TestPropertySource("classpath:global.properties")
public class ScheduleAbsoluteRouteInterconnectionResolverImplUnitTest {

	@Mock
	private RouteResolver routeResolver;

	@Mock
	private ScheduleProvider scheduleProvider;

	@Mock
	private ScheduledMonthFlight mockSmf;

	@Autowired
	@InjectMocks
	private ScheduleAbsoluteRouteInterconnectionResolverImplService absoluteRouteInterconnectionResolverImplService;

	private final Route route = Route.get("CPH", "MAD");

	private final static LocalDateTime TODAY = LocalDateTime.now();

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);

		when(routeResolver.resolve(eq(route.getOrigin()), eq(route.getDestination())))
				.thenReturn(Arrays.asList(new ResolvedRoute(route)));

		ScheduledDayFlight sdf = mock(ScheduledDayFlight.class);
		when(sdf.getArrivalTime()).thenReturn(TODAY.toLocalTime().plusHours(3));
		when(sdf.getDepartureTime()).thenReturn(TODAY.toLocalTime().minusHours(3));

		FullScheduledDay fsd = mock(FullScheduledDay.class);
		when(fsd.getDay()).thenReturn(TODAY.getDayOfMonth());
		when(fsd.getFlights()).thenReturn(Arrays.asList(sdf));
		when(mockSmf.getDays()).thenReturn(Arrays.asList(fsd));

		when(scheduleProvider.getFlights(route, YearMonth.from(TODAY))).thenReturn(Optional.of(mockSmf));
	}

	@Test
	public void testRouteFoundAndScheduleFound() {
		testRegularResolution(1, 1);
	}

	@Test
	public void testRouteFoundAndScheduleNotFound() {
		when(routeResolver.resolve(eq(route.getOrigin()), eq(route.getDestination()))).thenReturn(new ArrayList<>());
		testRegularResolution(0, 0);
	}

	private void testRegularResolution(int expectedResolvedSchedules, int invokeScheduleProvider) {
		List<ResolvedSchedule> rs = absoluteRouteInterconnectionResolverImplService.resolve(route, TODAY.withHour(1),
				TODAY.withHour(23));
		assertNotNull(rs);
		assertEquals(expectedResolvedSchedules, rs.size());
		verify(scheduleProvider, Mockito.times(invokeScheduleProvider)).getFlights(eq(route),
				eq(YearMonth.from(TODAY)));
		verify(routeResolver, Mockito.times(1)).resolve(eq(route.getOrigin()), eq(route.getDestination()));
	}
}
