package com.ryanair.alvaro.interconnectingflights.logic.scheduler;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.ryanair.alvaro.interconnectingflights.model.json.Route;
import com.ryanair.alvaro.interconnectingflights.model.json.ScheduledMonthFlight.FullScheduledDay;

/**
 * Builds up a list of ScheduledDateFlight based on the incoming FullScheduleDay
 * 
 * @author Alvaro Velasco Fernandez
 *
 */
@Service
public final class ScheduleDateFlightBuilderImpl implements ScheduleDateFlightBuilder {

	@Override
	public List<ScheduledDateFlight> build(Route route, YearMonth yearMonth, FullScheduledDay fullScheduleDay) {
		Objects.requireNonNull(route);
		Objects.requireNonNull(yearMonth);
		Objects.requireNonNull(fullScheduleDay);

		List<ScheduledDateFlight> scheduledDateFlights = fullScheduleDay.getFlights().stream().map(scheduleFlight -> {
			LocalDateTime departure = yearMonth.atDay(fullScheduleDay.getDay())
					.atTime(scheduleFlight.getDepartureTime());
			LocalDateTime arrival = departure.toLocalDate().atTime(scheduleFlight.getArrivalTime());

			// if the arrival time seems to be before the departure, it means to
			// be done the day after
			if (scheduleFlight.getArrivalTime().isBefore(scheduleFlight.getDepartureTime())) {
				arrival = arrival.plusDays(1);
			}

			return new ScheduledDateFlight(route, departure, arrival);
		}).collect(Collectors.toList());

		scheduledDateFlights.sort((schedule1, schedule2) -> schedule1.compareTo(schedule2));

		return scheduledDateFlights;
	}

}
