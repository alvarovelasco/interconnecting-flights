package com.ryanair.alvaro.interconnectingflights.logic.scheduler;

import java.time.YearMonth;
import java.util.List;

import com.ryanair.alvaro.interconnectingflights.model.json.Route;
import com.ryanair.alvaro.interconnectingflights.model.json.ScheduledMonthFlight.FullScheduledDay;

public interface ScheduleDateFlightBuilder {

	List<ScheduledDateFlight> build(Route route, YearMonth yearMonth, FullScheduledDay fullScheduledDay);

}
