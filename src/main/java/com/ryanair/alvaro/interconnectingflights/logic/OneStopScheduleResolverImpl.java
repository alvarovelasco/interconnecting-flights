package com.ryanair.alvaro.interconnectingflights.logic;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import com.ryanair.alvaro.interconnectingflights.logic.schedules.ScheduledDateFlight;
import com.ryanair.alvaro.interconnectingflights.logic.schedules.rules.ScheduleFlightArrivalBefore;
import com.ryanair.alvaro.interconnectingflights.logic.schedules.rules.ScheduleFlightDepartureAfter;
import com.ryanair.alvaro.interconnectingflights.logic.schedules.rules.ScheduleFlightDepartureGreaterThan;
import com.ryanair.alvaro.interconnectingflights.model.ResolvedRoute;
import com.ryanair.alvaro.interconnectingflights.model.ScheduledFlightAndRouteMap;
import com.ryanair.alvaro.interconnectingflights.model.json.ResolvedSchedule;
import com.ryanair.alvaro.interconnectingflights.model.json.Route;

@Service
public class OneStopScheduleResolverImpl implements ScheduleResolver {

	private static Logger logger = LogManager.getLogger(OneStopScheduleResolverImpl.class); 
	
	private ScheduledFlightAndRouteMap scheduledFlightAndRouteMap;

	private static final int HOURS_OFFSET_FLIGHT_STOP_OVER = 2;
	
	public void set(ScheduledFlightAndRouteMap scheduledFlightAndRouteMap) {
		this.scheduledFlightAndRouteMap = scheduledFlightAndRouteMap;
	}
	
	@Override
	public List<ResolvedSchedule> resolve(ResolvedRoute resolvedRoute, LocalDateTime from, LocalDateTime to) {
		logger.debug("resolved Schedules one-stop routes {} ", resolvedRoute);
		List<ResolvedSchedule> resolvedSchedules = new ArrayList<>();
		Route r1 = resolvedRoute.getRouteConcat().get(0);
		Route r2 = resolvedRoute.getRouteConcat().get(1);

		List<ScheduledDateFlight> validInBoundaries = scheduledFlightAndRouteMap.getFlights(r1).stream()
				.filter(ScheduleFlightDepartureAfter.at(from).and(ScheduleFlightArrivalBefore.at(to)))
				.collect(Collectors.toList());

		logger.debug("Valid boundaries {} in the first route {}", validInBoundaries, r1);
		
		final int EXPECTED_STOPS = 1;
		final ResolvedSchedule.Builder resolvedScheduleBuilder = new ResolvedSchedule.Builder(EXPECTED_STOPS);

		validInBoundaries.stream().forEach(s -> {
			resolvedScheduleBuilder.addFirstRoute(r1, s.getDepartureDateTime(), s.getArrivalDateTime());

			// get the valid time in boundaries for the second flight
			// Remove the schedule flights not adjusting to the offset hours
			// between the arrival of the first flight and the departure of the
			// second one
			List<ScheduledDateFlight> newRoute2ValidInBoundaries = scheduledFlightAndRouteMap.getFlights(r2).stream()
					.filter(ScheduleFlightDepartureAfter.at(from).
							and(ScheduleFlightArrivalBefore.at(to).
							and(ScheduleFlightDepartureGreaterThan.previousFlight(s, HOURS_OFFSET_FLIGHT_STOP_OVER))))
					.collect(Collectors.toList());
			logger.debug("Valid boundaries {} in the second route {}", newRoute2ValidInBoundaries, r2);
			// Add the second schedule flight in the second route with the remaining valid times.
			newRoute2ValidInBoundaries.stream()
				.filter(sFlight -> sFlight.getRoute().equals(r2))
					.map(s2 -> 
							resolvedScheduleBuilder.addSecondRoute(s2.getRoute(), s2.getDepartureDateTime(),
									s2.getArrivalDateTime()).build()
					).map(o -> o.orElse(null)).filter(rs -> rs != null)
					.collect(Collectors.toCollection(() -> resolvedSchedules));

			resolvedScheduleBuilder.reset();
		});
		
		return resolvedSchedules;
	}

}
