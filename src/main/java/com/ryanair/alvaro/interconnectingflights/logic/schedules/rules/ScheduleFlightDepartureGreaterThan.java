package com.ryanair.alvaro.interconnectingflights.logic.schedules.rules;

import static java.util.Objects.requireNonNull;

import java.time.LocalDateTime;

import com.ryanair.alvaro.interconnectingflights.logic.schedules.ScheduledDateFlight;

public class ScheduleFlightDepartureGreaterThan implements FlightRouteRule {

	private final ScheduledDateFlight previousFlight;

	private final int hoursOffset;

	public ScheduleFlightDepartureGreaterThan(ScheduledDateFlight previousFlight) {
		this(previousFlight, 0);
	}

	public ScheduleFlightDepartureGreaterThan(ScheduledDateFlight previousFlight, int hourOffset) {
		this.previousFlight = requireNonNull(previousFlight);
		this.hoursOffset = hourOffset;
	}

	@Override
	public boolean test(ScheduledDateFlight f) {
		LocalDateTime dateTime = previousFlight.getArrivalDateTime().plusHours(hoursOffset);
		return new ScheduleFlightDepartureAfter(dateTime).test(f);
	}

}
