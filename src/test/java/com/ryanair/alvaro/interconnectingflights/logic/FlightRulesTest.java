package com.ryanair.alvaro.interconnectingflights.logic;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import com.ryanair.alvaro.interconnectingflights.logic.schedules.ScheduledDateFlight;
import com.ryanair.alvaro.interconnectingflights.logic.schedules.rules.ScheduleFlightArrivalBefore;
import com.ryanair.alvaro.interconnectingflights.logic.schedules.rules.ScheduleFlightDepartureAfter;
import com.ryanair.alvaro.interconnectingflights.logic.schedules.rules.ScheduleFlightDepartureGreaterThan;
import com.ryanair.alvaro.interconnectingflights.model.json.Route;

@RunWith(MockitoJUnitRunner.class)
public class FlightRulesTest {

	@Test
	public void testArrivalBeforeRule() {
		ScheduledDateFlight sdf = new ScheduledDateFlight(Route.get("TEST", "SUC"), LocalDateTime.of(2000, 1, 1, 0, 0),
				LocalDateTime.of(2000, 1, 1, 0, 30));

		assertTrue(ScheduleFlightArrivalBefore.at(LocalDateTime.of(2000, 1, 1, 1, 0)).test(sdf));
	}

	@Test
	public void testArrivalBeforeRule_negativeResponse() {
		ScheduledDateFlight sdf = new ScheduledDateFlight(Route.get("TEST", "SUC"), LocalDateTime.of(2000, 1, 1, 0, 0),
				LocalDateTime.of(2000, 1, 1, 2, 30));

		assertFalse(ScheduleFlightArrivalBefore.at(LocalDateTime.of(2000, 1, 1, 2, 0)).test(sdf));
		assertFalse(ScheduleFlightArrivalBefore.at(LocalDateTime.of(2000, 1, 1, 2, 30)).test(sdf));
	}

	@Test
	public void testDepartureAfterRule() {
		ScheduledDateFlight sdf = new ScheduledDateFlight(Route.get("TEST", "SUC"), LocalDateTime.of(2000, 1, 1, 2, 0),
				LocalDateTime.of(2000, 1, 1, 2, 30));

		assertTrue(ScheduleFlightDepartureAfter.at(LocalDateTime.of(2000, 1, 1, 1, 0)).test(sdf));
	}

	@Test
	public void testDepartureAfterRule_negativeResponse() {
		ScheduledDateFlight sdf = new ScheduledDateFlight(Route.get("TEST", "SUC"), LocalDateTime.of(2000, 1, 1, 0, 0),
				LocalDateTime.of(2000, 1, 1, 2, 30));

		assertFalse(ScheduleFlightDepartureAfter.at(LocalDateTime.of(2000, 1, 1, 1, 0)).test(sdf));
		assertFalse(ScheduleFlightDepartureAfter.at(LocalDateTime.of(2000, 1, 1, 0, 0)).test(sdf));
	}

	@Test
	public void testTwoConsecutiveFlights_noOffset() {
		ScheduledDateFlight sdf1 = new ScheduledDateFlight(Route.get("TEST", "SUC"), LocalDateTime.of(2000, 1, 1, 0, 0),
				LocalDateTime.of(2000, 1, 1, 2, 30));
		ScheduledDateFlight sdf2 = new ScheduledDateFlight(Route.get("SUC", "PLA"), LocalDateTime.of(2000, 1, 1, 2, 31),
				LocalDateTime.of(2000, 1, 1, 3, 30));

		assertTrue(ScheduleFlightDepartureGreaterThan.previousFlight(sdf1).test(sdf2));
	}

	@Test
	public void testTwoConsecutiveFlights_2HourOffset() {
		ScheduledDateFlight sdf1 = new ScheduledDateFlight(Route.get("TEST", "SUC"), LocalDateTime.of(2000, 1, 1, 0, 0),
				LocalDateTime.of(2000, 1, 1, 2, 30));
		ScheduledDateFlight sdf2 = new ScheduledDateFlight(Route.get("SUC", "PLA"), LocalDateTime.of(2000, 1, 1, 5, 30),
				LocalDateTime.of(2000, 1, 1, 6, 30));

		assertTrue(ScheduleFlightDepartureGreaterThan.previousFlight(sdf1, 2).test(sdf2));
	}
	
	@Test
	public void testTwoConsecutiveFlights_1HourOffset_badConnection() {
		ScheduledDateFlight sdf1 = new ScheduledDateFlight(Route.get("TEST", "SUC"), LocalDateTime.of(2000, 1, 1, 0, 0),
				LocalDateTime.of(2000, 1, 1, 2, 30));
		ScheduledDateFlight sdf2 = new ScheduledDateFlight(Route.get("SUC", "PLA"), LocalDateTime.of(2000, 1, 1, 2, 45),
				LocalDateTime.of(2000, 1, 1, 6, 30));

		assertFalse(ScheduleFlightDepartureGreaterThan.previousFlight(sdf1, 1).test(sdf2));
	}
}
