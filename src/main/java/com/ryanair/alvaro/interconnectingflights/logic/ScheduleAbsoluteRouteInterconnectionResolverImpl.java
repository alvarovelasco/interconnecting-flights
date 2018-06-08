package com.ryanair.alvaro.interconnectingflights.logic;

import static java.util.Objects.requireNonNull;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ryanair.alvaro.interconnectingflights.logic.route.RouteResolver;
import com.ryanair.alvaro.interconnectingflights.logic.scheduler.ScheduleDateFlightBuilder;
import com.ryanair.alvaro.interconnectingflights.logic.scheduler.ScheduleProvider;
import com.ryanair.alvaro.interconnectingflights.logic.scheduler.ScheduledDateFlight;
import com.ryanair.alvaro.interconnectingflights.logic.scheduler.rules.ScheduleFlightArrivalBefore;
import com.ryanair.alvaro.interconnectingflights.logic.scheduler.rules.ScheduleFlightDepartureAfter;
import com.ryanair.alvaro.interconnectingflights.logic.scheduler.rules.ScheduleFlightDepartureGreaterThan;
import com.ryanair.alvaro.interconnectingflights.model.ResolvedRoute;
import com.ryanair.alvaro.interconnectingflights.model.ResolvedSchedule;
import com.ryanair.alvaro.interconnectingflights.model.json.Route;
import com.ryanair.alvaro.interconnectingflights.model.json.ScheduledMonthFlight;

@Service
public final class ScheduleAbsoluteRouteInterconnectionResolverImpl
		implements ScheduleAbsoluteRouteInterconnectionResolver {

	@Autowired
	private RouteResolver routeResolver;

	@Autowired
	private ScheduleProvider scheduleProvider;

	@Autowired
	private ScheduleDateFlightBuilder scheduleDateFlightBuilder;

	private static final int HOURS_OFFSET_FLIGHT_STOP_OVER = 2;

	@Override
	public List<ResolvedSchedule> resolve(Route route, LocalDateTime from, LocalDateTime to) {
		requireNonNull(route);
		// 1. get the routes to follow route
		List<ResolvedRoute> resolvedRoutes = routeResolver.resolve(route.getOrigin(), route.getDestination());

		// 2. Decompound the full routes into all possible routes.
		Set<Route> allExistingRoutesInvolved = resolvedRoutes.stream().flatMap(rr -> rr.getRouteConcat().stream())
				.distinct().collect(Collectors.toSet());

		// 3. calculate the year/months that might be appliable
		List<YearMonth> allYearMonthAppliable = getAllYearMonthIn(from.toLocalDate(), to.toLocalDate());

		// 4. Get the schedule flight date time per route and year/month and
		// store in a map.
		Map<Route, List<ScheduledDateFlight>> allExistingFlightsPerRouteInTheTimeRangeProvided = mapExistingRoutesInvolved(
				allExistingRoutesInvolved, allYearMonthAppliable);

		// 5. Back to full route, in each route involved, iterate the schedule
		// flights, and once an element compulses the from datetime, take
		// and compare to the rest of the items in the list until an item is
		// after to
		// * If the full route has stop-over, apply different function:
		// iterate the schedule, take the element compulsing from datetime, then
		// take the next route and iterate from its schedules having >2h
		// +arrival
		List<ResolvedSchedule> resolvedSchedules = new ArrayList<>();
		List<ResolvedSchedule> newResolvedSchedules;

		for (ResolvedRoute resolvedRoute : resolvedRoutes) {
			if (resolvedRoute.getRouteConcat().size() == 1) {
				newResolvedSchedules = getResolvedSchedulesNonStopRoute(from, to,
						allExistingFlightsPerRouteInTheTimeRangeProvided, resolvedRoute);
			} else {
				newResolvedSchedules = getResolvedSchedulesStopRoute(from, to,
						allExistingFlightsPerRouteInTheTimeRangeProvided, resolvedRoute);
			}

			resolvedSchedules.addAll(newResolvedSchedules);
		}

		return resolvedSchedules;
	}

	public Map<Route, List<ScheduledDateFlight>> mapExistingRoutesInvolved(Set<Route> allExistingRoutesInvolved,
			List<YearMonth> allYearMonthAppliable) {

		Map<Route, List<ScheduledDateFlight>> allExistingFlightsPerRouteInTheTimeRangeProvided = new HashMap<>();
		for (Route existingRoute : allExistingRoutesInvolved) {

			List<ScheduledDateFlight> scheduleDateFlights = allYearMonthAppliable.stream().map(ym -> {
				Optional<ScheduledMonthFlight> smf = scheduleProvider.getFlights(existingRoute, ym);
				if (!smf.isPresent()) {
					return null;
				}

				return smf.get().getDays().stream()
						.map(fullScheduledDay -> scheduleDateFlightBuilder.build(existingRoute, ym, fullScheduledDay))
						.collect(Collectors.toList());

			}).filter(s -> s != null).flatMap(List::stream).flatMap(List::stream).collect(Collectors.toList());

			allExistingFlightsPerRouteInTheTimeRangeProvided.put(existingRoute, scheduleDateFlights);
		}

		return allExistingFlightsPerRouteInTheTimeRangeProvided;
	}

	private List<ResolvedSchedule> getResolvedSchedulesNonStopRoute(LocalDateTime from, LocalDateTime to,
			Map<Route, List<ScheduledDateFlight>> allExistingFlightsPerRouteInTheTimeRangeProvided,
			ResolvedRoute resolvedRoute) {
		Route r = resolvedRoute.getRouteConcat().get(0);

		List<ScheduledDateFlight> validInBoundaries = allExistingFlightsPerRouteInTheTimeRangeProvided.get(r).stream()
				.filter(new ScheduleFlightDepartureAfter(from).and(new ScheduleFlightArrivalBefore(to)))
				.collect(Collectors.toList());

		return validInBoundaries.stream()
				.map(s -> new ResolvedSchedule.Builder(0)
						.addFirstRoute(r, s.getDepartureDateTime(), s.getArrivalDateTime()).build())
				.map(o -> o.orElse(null)).filter(rs -> rs != null).collect(Collectors.toList());
	}

	/*
	 * Resolves the schedules in routes with stop-over
	 */
	private List<ResolvedSchedule> getResolvedSchedulesStopRoute(LocalDateTime from, LocalDateTime to,
			Map<Route, List<ScheduledDateFlight>> allExistingFlightsPerRouteInTheTimeRangeProvided,
			ResolvedRoute resolvedRoute) {
		List<ResolvedSchedule> resolvedSchedules = new ArrayList<>();
		Route r1 = resolvedRoute.getRouteConcat().get(0);

		List<ScheduledDateFlight> validInBoundaries = allExistingFlightsPerRouteInTheTimeRangeProvided.get(r1).stream()
				.filter(new ScheduleFlightDepartureAfter(from).and(new ScheduleFlightArrivalBefore(to)))
				.collect(Collectors.toList());
		final ResolvedSchedule.Builder resolvedScheduleBuilder = new ResolvedSchedule.Builder(1);
		List<ScheduledDateFlight> newRoute2ValidInBoundaries = null;
		Route r2 = resolvedRoute.getRouteConcat().get(1);

		for (ScheduledDateFlight s : validInBoundaries) {
			resolvedScheduleBuilder.addFirstRoute(r1, s.getDepartureDateTime(), s.getArrivalDateTime());

			newRoute2ValidInBoundaries = allExistingFlightsPerRouteInTheTimeRangeProvided.get(r2).stream()
					.filter(new ScheduleFlightDepartureAfter(from).and(new ScheduleFlightArrivalBefore(to)))
					.collect(Collectors.toList());
			// Remove the schedule flights not adjusting to the offset hours
			// between the arrival of the first flight and the departure of the
			// second one
			newRoute2ValidInBoundaries.stream()
					.filter(new ScheduleFlightDepartureGreaterThan(s, HOURS_OFFSET_FLIGHT_STOP_OVER)).map(s2 -> {
						if (r2.equals(s2.getRoute())) {
							resolvedScheduleBuilder.addSecondRoute(s2.getRoute(), s2.getDepartureDateTime(),
									s2.getArrivalDateTime());
						}
						return resolvedScheduleBuilder.build();
					}).map(o -> o.orElse(null)).filter(rs -> rs != null)
					.collect(Collectors.toCollection(() -> resolvedSchedules));

			resolvedScheduleBuilder.reset();
		}

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
