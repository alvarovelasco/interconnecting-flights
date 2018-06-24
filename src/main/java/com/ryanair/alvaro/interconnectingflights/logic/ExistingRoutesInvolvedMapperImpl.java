package com.ryanair.alvaro.interconnectingflights.logic;

import java.time.YearMonth;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ryanair.alvaro.interconnectingflights.logic.schedules.ScheduleDateFlightBuilder;
import com.ryanair.alvaro.interconnectingflights.logic.schedules.ScheduledDateFlight;
import com.ryanair.alvaro.interconnectingflights.model.ScheduledFlightAndRouteMap;
import com.ryanair.alvaro.interconnectingflights.model.ScheduledFlightAndRouteMap.ScheduledFlightAndRouteMapBuilder;
import com.ryanair.alvaro.interconnectingflights.model.json.Route;
import com.ryanair.alvaro.interconnectingflights.model.json.ScheduledMonthFlight;
import com.ryanair.alvaro.interconnectingflights.provider.ScheduleProvider;

@Service
public class ExistingRoutesInvolvedMapperImpl implements ExistingRoutesInvolvedMapper {
	
	@Autowired
	private ScheduleProvider scheduleProvider;
	
	@Autowired
	private ScheduleDateFlightBuilder scheduleDateFlightBuilder;
	
	@Override
	public ScheduledFlightAndRouteMap map(Set<Route> allExistingRoutesInvolved, List<YearMonth> allYearMonthAppliable) {
		ScheduledFlightAndRouteMapBuilder builder = ScheduledFlightAndRouteMapBuilder.instance();
		
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

			builder.set(existingRoute).add(scheduleDateFlights);
		}

		return builder.build();
	}

}
