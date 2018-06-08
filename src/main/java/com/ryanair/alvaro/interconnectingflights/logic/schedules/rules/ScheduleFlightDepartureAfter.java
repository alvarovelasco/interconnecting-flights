package com.ryanair.alvaro.interconnectingflights.logic.schedules.rules;

import static java.util.Objects.requireNonNull;

import java.time.LocalDateTime;

import com.ryanair.alvaro.interconnectingflights.logic.schedules.ScheduledDateFlight;

public final class ScheduleFlightDepartureAfter implements FlightRouteRule {

	private final LocalDateTime referenceDateTime;

	private final int hoursOffset;

	public ScheduleFlightDepartureAfter(LocalDateTime referenceDateTime) {
		this(referenceDateTime, 0);
	}

	public ScheduleFlightDepartureAfter(LocalDateTime referenceDateTime, int hourOffset) {
		this.referenceDateTime = requireNonNull(referenceDateTime);
		this.hoursOffset = hourOffset;
	}

	@Override
	public boolean test(ScheduledDateFlight f) {
		LocalDateTime dateTime = referenceDateTime.plusHours(hoursOffset);

		return f.getDepartureDateTime().isAfter(dateTime);
	}
}
