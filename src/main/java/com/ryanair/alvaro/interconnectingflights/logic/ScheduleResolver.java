package com.ryanair.alvaro.interconnectingflights.logic;

import java.time.LocalDateTime;
import java.util.List;

import com.ryanair.alvaro.interconnectingflights.model.ResolvedRoute;
import com.ryanair.alvaro.interconnectingflights.model.json.ResolvedSchedule;

public interface ScheduleResolver extends ScheduledFlightAndRouteMapContextSetter {
	
	List<ResolvedSchedule> resolve(ResolvedRoute resolvedRoute, LocalDateTime from, LocalDateTime to);
	
}
