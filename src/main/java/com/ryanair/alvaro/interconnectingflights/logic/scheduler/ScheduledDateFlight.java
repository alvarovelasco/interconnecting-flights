package com.ryanair.alvaro.interconnectingflights.logic.scheduler;

import static java.util.Objects.requireNonNull;

import java.time.LocalDateTime;

import com.ryanair.alvaro.interconnectingflights.model.json.Route;

public final class ScheduledDateFlight implements Comparable<ScheduledDateFlight> {

	private final Route route;

	private final LocalDateTime departureDateTime;

	private final LocalDateTime arrivalDateTime;

	public ScheduledDateFlight(Route route, LocalDateTime departureDateTime, LocalDateTime arrivalDateTime) {
		this.route = requireNonNull(route);
		this.departureDateTime = requireNonNull(departureDateTime);
		this.arrivalDateTime = requireNonNull(arrivalDateTime);

		if (departureDateTime.isAfter(arrivalDateTime)) {
			throw new IllegalArgumentException("Departure must happen before landing");
		}
	}

	public LocalDateTime getArrivalDateTime() {
		return arrivalDateTime;
	}

	public LocalDateTime getDepartureDateTime() {
		return departureDateTime;
	}

	public Route getRoute() {
		return route;
	}

	@Override
	public int compareTo(ScheduledDateFlight otherScheduleDate) {
		return departureDateTime.compareTo(otherScheduleDate.getDepartureDateTime());
	}

	@Override
	public String toString() {
		return "ScheduledDateFlight [route=" + route + ", departureDateTime=" + departureDateTime + ", arrivalDateTime="
				+ arrivalDateTime + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((arrivalDateTime == null) ? 0 : arrivalDateTime.hashCode());
		result = prime * result + ((departureDateTime == null) ? 0 : departureDateTime.hashCode());
		result = prime * result + ((route == null) ? 0 : route.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ScheduledDateFlight other = (ScheduledDateFlight) obj;
		if (arrivalDateTime == null) {
			if (other.arrivalDateTime != null)
				return false;
		} else if (!arrivalDateTime.equals(other.arrivalDateTime))
			return false;
		if (departureDateTime == null) {
			if (other.departureDateTime != null)
				return false;
		} else if (!departureDateTime.equals(other.departureDateTime))
			return false;
		if (route == null) {
			if (other.route != null)
				return false;
		} else if (!route.equals(other.route))
			return false;
		return true;
	}

}
