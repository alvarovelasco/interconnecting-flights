package com.ryanair.alvaro.interconnectingflights.logic.schedules.rules;

import static java.util.Objects.requireNonNull;

import java.time.LocalDateTime;

import com.ryanair.alvaro.interconnectingflights.logic.schedules.ScheduledDateFlight;

public final class ScheduleFlightArrivalBefore implements FlightRouteRule {
	private final LocalDateTime referenceDateTime;

	private ScheduleFlightArrivalBefore(LocalDateTime referenceDateTime) {
		this.referenceDateTime = requireNonNull(referenceDateTime);
	}

	public static FlightRouteRule at(LocalDateTime reference) {
		return new ScheduleFlightArrivalBefore(reference);
	}

	@Override
	public boolean test(ScheduledDateFlight f) {
		return f.getArrivalDateTime().isBefore(referenceDateTime);
	}
}
