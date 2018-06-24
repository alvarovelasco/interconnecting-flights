package com.ryanair.alvaro.interconnectingflights.logic;

import com.ryanair.alvaro.interconnectingflights.model.ResolvedRoute;

public interface ResolvedSchedulesDispatcher extends ScheduledFlightAndRouteMapContextSetter {
	ScheduleResolver dispatch(ResolvedRoute resolvedRoutes);
}
