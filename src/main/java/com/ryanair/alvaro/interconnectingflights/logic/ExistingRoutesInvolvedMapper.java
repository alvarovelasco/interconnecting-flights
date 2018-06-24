package com.ryanair.alvaro.interconnectingflights.logic;

import java.time.YearMonth;
import java.util.List;
import java.util.Set;

import com.ryanair.alvaro.interconnectingflights.model.ScheduledFlightAndRouteMap;
import com.ryanair.alvaro.interconnectingflights.model.json.Route;

public interface ExistingRoutesInvolvedMapper {

	ScheduledFlightAndRouteMap map(Set<Route> allExistingRoutesInvolved,
			List<YearMonth> allYearMonthAppliable);
	
}
