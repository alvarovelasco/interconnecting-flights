package com.ryanair.alvaro.interconnectingflights.logic.schedules.rules;

import java.util.function.Predicate;

import com.ryanair.alvaro.interconnectingflights.logic.schedules.ScheduledDateFlight;

public interface FlightRouteRule extends Predicate<ScheduledDateFlight> {

	@Override
	default boolean test(ScheduledDateFlight t) {
		return true;
	}
}
