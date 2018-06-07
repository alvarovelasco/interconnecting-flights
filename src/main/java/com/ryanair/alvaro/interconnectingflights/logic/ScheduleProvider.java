package com.ryanair.alvaro.interconnectingflights.logic;

import java.time.YearMonth;

import com.ryanair.alvaro.interconnectingflights.model.Route;
import com.ryanair.alvaro.interconnectingflights.model.ScheduledMonthFlight;

public interface ScheduleProvider {
	public ScheduledMonthFlight getFlights(Route route, YearMonth yearMonth);
}
