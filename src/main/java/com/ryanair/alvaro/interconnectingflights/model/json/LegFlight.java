package com.ryanair.alvaro.interconnectingflights.model.json;

import static java.util.Objects.requireNonNull;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LegFlight {

	@JsonProperty("departureAirport")
	private final String departureAirport;

	@JsonProperty("arrivalAirport")
	private final String arrivalAirport;

	@JsonProperty("departureDateTime")
	private final LocalDateTime departure;

	@JsonProperty("arrivalDateTime")
	private final LocalDateTime arrival;

	public LegFlight(String departureAirport, String arrivalAirport, LocalDateTime departure, LocalDateTime arrival) {
		this.departureAirport = requireNonNull(departureAirport);
		this.arrivalAirport = requireNonNull(arrivalAirport);
		this.departure = requireNonNull(departure);
		this.arrival = requireNonNull(arrival);
	}

	@Override
	public String toString() {
		return "LegFlight [departureAirport=" + departureAirport + ", arrivalAirport=" + arrivalAirport + ", departure="
				+ departure + ", arrival=" + arrival + "]";
	}

}
