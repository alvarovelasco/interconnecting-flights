package com.ryanair.alvaro.interconnectingflights.provider;

import java.time.YearMonth;
import java.util.Optional;

import com.ryanair.alvaro.interconnectingflights.model.json.Route;
import com.ryanair.alvaro.interconnectingflights.model.json.ScheduledMonthFlight;

public interface ScheduleProvider {
	public Optional<ScheduledMonthFlight> getFlights(Route route, YearMonth yearMonth);
}
