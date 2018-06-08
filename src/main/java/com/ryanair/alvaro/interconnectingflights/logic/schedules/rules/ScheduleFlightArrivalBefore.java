package com.ryanair.alvaro.interconnectingflights.logic.schedules.rules;

import static java.util.Objects.requireNonNull;

import java.time.LocalDateTime;

import com.ryanair.alvaro.interconnectingflights.logic.schedules.ScheduledDateFlight;

public final class ScheduleFlightArrivalBefore implements FlightRouteRule {
	private final LocalDateTime referenceDateTime;

	private final int hoursOffset;

	public ScheduleFlightArrivalBefore(LocalDateTime referenceDateTime) {
		this(referenceDateTime, 0);
	}

	public ScheduleFlightArrivalBefore(LocalDateTime referenceDateTime, int hourOffset) {
		this.referenceDateTime = requireNonNull(referenceDateTime);
		this.hoursOffset = hourOffset;
	}

	@Override
	public boolean test(ScheduledDateFlight f) {
		LocalDateTime dateTime = referenceDateTime.plusHours(hoursOffset);

		return f.getArrivalDateTime().isBefore(dateTime);
	}
}
