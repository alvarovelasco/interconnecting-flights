package com.ryanair.alvaro.interconnectingflights.model.json;

import java.util.Objects;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Route {

	@JsonProperty("airportFrom")
	private String origin;

	@JsonProperty("airportTo")
	private String destination;

	private String connectingAirport;

	// To make it prettier and robust, this can be refactored using a builder
	// pattern
	public static final Route get(String origin, String destination, Optional<String> connectingAirport) {
		Route route = new Route();

		route.origin = Objects.requireNonNull(origin);
		route.destination = Objects.requireNonNull(destination);
		route.connectingAirport = Objects.requireNonNull(connectingAirport).orElse(null);

		return route;
	}

	public String getOrigin() {
		return origin;
	}

	public String getDestination() {
		return destination;
	}

	public boolean noConnectingAirport() {
		return connectingAirport == null;
	}

	@Override
	public String toString() {
		return "Route [origin=" + origin + ", destination=" + destination + ", connectingAirport=" + connectingAirport
				+ "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((connectingAirport == null) ? 0 : connectingAirport.hashCode());
		result = prime * result + ((destination == null) ? 0 : destination.hashCode());
		result = prime * result + ((origin == null) ? 0 : origin.hashCode());
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
		Route other = (Route) obj;
		if (connectingAirport == null) {
			if (other.connectingAirport != null)
				return false;
		} else if (!connectingAirport.equals(other.connectingAirport))
			return false;
		if (destination == null) {
			if (other.destination != null)
				return false;
		} else if (!destination.equals(other.destination))
			return false;
		if (origin == null) {
			if (other.origin != null)
				return false;
		} else if (!origin.equals(other.origin))
			return false;
		return true;
	}

}
