package com.ryanair.alvaro.interconnectingflights.logic;

import java.time.LocalDateTime;
import java.util.List;

import com.ryanair.alvaro.interconnectingflights.model.ResolvedSchedule;
import com.ryanair.alvaro.interconnectingflights.model.json.Route;

/**
 * Resolves and fetches all the routes and one-stop routes, with its scheduled
 * flights within a range of time
 * 
 * @author Alvaro Velasco Fernandez
 *
 */
public interface ScheduleAbsoluteRouteInterconnectionResolver {
	List<ResolvedSchedule> resolve(Route route, LocalDateTime from, LocalDateTime to);
}
