package com.ryanair.alvaro.interconnectingflights.logic;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ryanair.alvaro.interconnectingflights.logic.schedules.ScheduledDateFlight;
import com.ryanair.alvaro.interconnectingflights.logic.schedules.rules.ScheduleFlightArrivalBefore;
import com.ryanair.alvaro.interconnectingflights.logic.schedules.rules.ScheduleFlightDepartureAfter;
import com.ryanair.alvaro.interconnectingflights.model.ResolvedRoute;
import com.ryanair.alvaro.interconnectingflights.model.ScheduledFlightAndRouteMap;
import com.ryanair.alvaro.interconnectingflights.model.json.ResolvedSchedule;
import com.ryanair.alvaro.interconnectingflights.model.json.Route;

@Service
public class NonStopScheduleResolverImpl implements ScheduleResolver {

	private static Logger logger = LogManager.getLogger(NonStopScheduleResolverImpl.class); 
	
	private ScheduledFlightAndRouteMap scheduledFlightAndRouteMap;
	
	public void set(ScheduledFlightAndRouteMap scheduledFlightAndRouteMap) {
		this.scheduledFlightAndRouteMap = scheduledFlightAndRouteMap;
	}
	
	@Override
	public List<ResolvedSchedule> resolve(ResolvedRoute resolvedRoute, LocalDateTime from, LocalDateTime to) {
		Route r = resolvedRoute.getRouteConcat().get(0);
		logger.debug("resolve schedules non-stop route {}", r);
		
		List<ScheduledDateFlight> validInBoundaries = scheduledFlightAndRouteMap.getFlights(r).stream()
				.filter(ScheduleFlightDepartureAfter.at(from).and(ScheduleFlightArrivalBefore.at(to)))
				.collect(Collectors.toList());
		logger.debug("Valid boundaries {} in the first route {}", validInBoundaries, r);
		
		return validInBoundaries.stream()
				.map(s -> new ResolvedSchedule.Builder(0)
						.addFirstRoute(r, s.getDepartureDateTime(), s.getArrivalDateTime()).build())
				.map(o -> o.orElse(null)).filter(rs -> rs != null).collect(Collectors.toList());
	}

}
