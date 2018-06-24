package com.ryanair.alvaro.interconnectingflights.logic;

import static java.util.Objects.requireNonNull;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.impl.Log4jContextFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ryanair.alvaro.interconnectingflights.logic.route.RouteResolver;
import com.ryanair.alvaro.interconnectingflights.logic.schedules.ScheduleDateFlightBuilder;
import com.ryanair.alvaro.interconnectingflights.logic.schedules.ScheduledDateFlight;
import com.ryanair.alvaro.interconnectingflights.logic.schedules.rules.ScheduleFlightArrivalBefore;
import com.ryanair.alvaro.interconnectingflights.logic.schedules.rules.ScheduleFlightDepartureAfter;
import com.ryanair.alvaro.interconnectingflights.logic.schedules.rules.ScheduleFlightDepartureGreaterThan;
import com.ryanair.alvaro.interconnectingflights.model.ResolvedRoute;
import com.ryanair.alvaro.interconnectingflights.model.ScheduledFlightAndRouteMap;
import com.ryanair.alvaro.interconnectingflights.model.ScheduledFlightAndRouteMap.ScheduledFlightAndRouteMapBuilder;
import com.ryanair.alvaro.interconnectingflights.model.json.ResolvedSchedule;
import com.ryanair.alvaro.interconnectingflights.model.json.Route;
import com.ryanair.alvaro.interconnectingflights.model.json.ScheduledMonthFlight;
import com.ryanair.alvaro.interconnectingflights.provider.ScheduleProvider;

@Service
public class ScheduleAbsoluteRouteInterconnectionResolverImplService
		implements ScheduleAbsoluteRouteInterconnectionResolver {

	private static Logger logger = LogManager.getLogger(ScheduleAbsoluteRouteInterconnectionResolverImplService.class); 
	
	@Autowired
	private RouteResolver routeResolver;

	@Autowired
	private ExistingRoutesInvolvedMapper existingRoutesInvolvedMapper;
	
	@Autowired
	private ResolvedSchedulesDispatcher resolvedSchedulesDispatcher;
	

	@Override
	public List<ResolvedSchedule> resolve(Route route, LocalDateTime from, LocalDateTime to) {
		requireNonNull(route);
		final List<ResolvedSchedule> resolvedSchedules = new ArrayList<>();
		// 1. get the routes to follow route
		List<ResolvedRoute> resolvedRoutes = routeResolver.resolve(route.getOrigin(), route.getDestination());
		
		// No route found, why to continue
		if (resolvedRoutes.isEmpty()) {
			return resolvedSchedules;
		}
		// 2. Decompound the full routes into all possible routes.
		Set<Route> allExistingRoutesInvolved = resolvedRoutes.stream().flatMap(rr -> rr.getRouteConcat().stream())
				.distinct().collect(Collectors.toSet());

		// 3. calculate the year/months that might be appliable
		List<YearMonth> allYearMonthAppliable = getAllYearMonthIn(from.toLocalDate(), to.toLocalDate());

		// 4. Get the schedule flight date time per route and year/month and
		// store in a map.
		ScheduledFlightAndRouteMap scheduledFlightRouteMap = existingRoutesInvolvedMapper.map(allExistingRoutesInvolved, allYearMonthAppliable);
		
		logger.debug("existing flights per route in time {}", scheduledFlightRouteMap);
		
		// 5. Back to full route, in each route involved, iterate the schedule
		// flights, and once an element compulses the from datetime, take
		// and compare to the rest of the items in the list until an item is
		// after to
		// * If the full route has stop-over, apply different function:
		// iterate the schedule, take the element compulsing from datetime, then
		// take the next route and iterate from its schedules having >2h
		// +arrival
		resolvedSchedulesDispatcher.set(scheduledFlightRouteMap);
		resolvedRoutes.stream().map(resolvedRoute -> {
			return resolvedSchedulesDispatcher.dispatch(resolvedRoute).resolve(resolvedRoute, from, to);
		}).flatMap(List::stream).collect(Collectors.toCollection(() -> resolvedSchedules));;
		
		Collections.sort(resolvedSchedules);
		logger.debug("Resolved schedules {}", resolvedSchedules);

		return resolvedSchedules;
	}


	public static List<YearMonth> getAllYearMonthIn(LocalDate from, LocalDate to) {
		List<YearMonth> allYearMonthAppliable = new ArrayList<>();

		Period periodBetween = Period.between(from, to);

		allYearMonthAppliable.add(YearMonth.from(from));
		if (periodBetween.getMonths() > 0) {
			IntStream.range(1, periodBetween.getMonths() + 1).mapToObj(i -> YearMonth.from(from).plusMonths(i))
					.collect(Collectors.toCollection(() -> allYearMonthAppliable));
		}

		return allYearMonthAppliable;
	}
}
