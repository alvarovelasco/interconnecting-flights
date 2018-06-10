package com.ryanair.alvaro.interconnectingflights.logic.schedules.rules;

import static java.util.Objects.requireNonNull;

import java.time.LocalDateTime;

import com.ryanair.alvaro.interconnectingflights.logic.schedules.ScheduledDateFlight;

public class ScheduleFlightDepartureGreaterThan implements FlightRouteRule {

	private final ScheduledDateFlight previousFlight;

	private final int hoursOffset;

	private ScheduleFlightDepartureGreaterThan(ScheduledDateFlight previousFlight) {
		this(previousFlight, 0);
	}

	private ScheduleFlightDepartureGreaterThan(ScheduledDateFlight previousFlight, int hourOffset) {
		this.previousFlight = requireNonNull(previousFlight);
		this.hoursOffset = hourOffset;
	}

	public static FlightRouteRule previousFlight(ScheduledDateFlight flight) {
		return new ScheduleFlightDepartureGreaterThan(flight);
	}

	public static FlightRouteRule previousFlight(ScheduledDateFlight flight, int hourOffset) {
		return new ScheduleFlightDepartureGreaterThan(flight, hourOffset);
	}

	@Override
	public boolean test(ScheduledDateFlight f) {
		LocalDateTime dateTime = previousFlight.getArrivalDateTime().plusHours(hoursOffset);
		return ScheduleFlightDepartureAfter.at(dateTime).test(f);
	}

}
