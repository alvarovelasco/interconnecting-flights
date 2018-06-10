package com.ryanair.alvaro.interconnectingflights.logic.schedules.rules;

import static java.util.Objects.requireNonNull;

import java.time.LocalDateTime;

import com.ryanair.alvaro.interconnectingflights.logic.schedules.ScheduledDateFlight;

public final class ScheduleFlightDepartureAfter implements FlightRouteRule {

	private final LocalDateTime referenceDateTime;

	private ScheduleFlightDepartureAfter(LocalDateTime referenceDateTime) {
		this.referenceDateTime = requireNonNull(referenceDateTime);
	}

	public static FlightRouteRule at(LocalDateTime reference) {
		return new ScheduleFlightDepartureAfter(reference);
	}

	@Override
	public boolean test(ScheduledDateFlight f) {
		return f.getDepartureDateTime().isAfter(referenceDateTime);
	}
}
