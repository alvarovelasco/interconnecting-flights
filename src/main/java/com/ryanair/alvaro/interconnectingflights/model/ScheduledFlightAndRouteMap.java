package com.ryanair.alvaro.interconnectingflights.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.ryanair.alvaro.interconnectingflights.logic.schedules.ScheduledDateFlight;
import com.ryanair.alvaro.interconnectingflights.model.json.Route;

public class ScheduledFlightAndRouteMap {

	private final Map<Route, List<ScheduledDateFlight>> mapRouteAndScheduledFlights;
	
	private ScheduledFlightAndRouteMap(Map<Route, List<ScheduledDateFlight>> mapRouteAndScheduledFlights) {
		this.mapRouteAndScheduledFlights = Objects.requireNonNull(mapRouteAndScheduledFlights);
	}
	
	public List<ScheduledDateFlight> getFlights(Route route) {
		return new ArrayList<>(mapRouteAndScheduledFlights.get(route));
	}
	
	@Override
	public String toString() {
		return "ScheduledFlightAndRouteMap [mapRouteAndScheduledFlights=" + mapRouteAndScheduledFlights + "]";
	}

	public static class ScheduledFlightAndRouteMapBuilder {
		
		Map<Route, List<ScheduledDateFlight>> mapRouteAndScheduledFlights = new HashMap<>();
		
		private Route currentRoute;
		
		private ScheduledFlightAndRouteMapBuilder() {
		}
		
		public static ScheduledFlightAndRouteMapBuilder instance() { 
			return new ScheduledFlightAndRouteMapBuilder();
		}
		
		public ScheduledFlightAndRouteMapBuilder set(Route route) {
			this.currentRoute = Objects.requireNonNull(route);
			return this;
		}
		
		public ScheduledFlightAndRouteMapBuilder add(List<ScheduledDateFlight> scheduledDateFlights) { 
			if (currentRoute == null) 
				throw new IllegalStateException("Current route not defined");
			
			mapRouteAndScheduledFlights.put(currentRoute, scheduledDateFlights);
			
			return this;
		}
		
		public ScheduledFlightAndRouteMap build() {
			return new ScheduledFlightAndRouteMap(mapRouteAndScheduledFlights);
		}
		
	}
}
