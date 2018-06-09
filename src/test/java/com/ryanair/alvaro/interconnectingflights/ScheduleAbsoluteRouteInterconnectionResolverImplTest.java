package com.ryanair.alvaro.interconnectingflights;

import static org.junit.Assert.assertNotNull;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import com.ryanair.alvaro.interconnectingflights.logic.ScheduleAbsoluteRouteInterconnectionResolverImplService;
import com.ryanair.alvaro.interconnectingflights.model.json.ResolvedSchedule;
import com.ryanair.alvaro.interconnectingflights.model.json.Route;

@RunWith(SpringRunner.class)
@SpringBootTest
@Import(TestConfig.class)
@TestPropertySource("classpath:global.properties")
public class ScheduleAbsoluteRouteInterconnectionResolverImplTest {

	@Autowired
	RestTemplate restTemplate;

	@Autowired
	private ScheduleAbsoluteRouteInterconnectionResolverImplService scheduleAbsoluteRouteResolverImpl;

	@Test
	public void testRyanairInterconnectionScheduleResolver() {
		Route routeManAlc = Route.get("MAN", "ALC");
		LocalDateTime departure = LocalDateTime.parse("2018-06-17T06:00", DateTimeFormatter.ISO_LOCAL_DATE_TIME);
		LocalDateTime arrival = LocalDateTime.parse("2018-06-17T23:45", DateTimeFormatter.ISO_LOCAL_DATE_TIME);

		List<ResolvedSchedule> routes = scheduleAbsoluteRouteResolverImpl.resolve(routeManAlc, departure, arrival);

		assertNotNull(routes);
		System.out.println(routes);
	}

}
