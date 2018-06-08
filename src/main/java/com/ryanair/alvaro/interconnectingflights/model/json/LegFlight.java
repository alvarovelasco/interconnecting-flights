package com.ryanair.alvaro.interconnectingflights.model.json;

import static java.util.Objects.requireNonNull;

import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

public class LegFlight {

	@JsonProperty("departureAirport")
	private final String departureAirport;

	@JsonProperty("arrivalAirport")
	private final String arrivalAirport;

	@JsonProperty("departureDateTime")
	@JsonFormat(timezone = "UTC", pattern = "yyyy-MM-dd'T'HH:mm")
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	private final LocalDateTime departure;

	@JsonProperty("arrivalDateTime")
	@JsonFormat(timezone = "UTC", pattern = "yyyy-MM-dd'T'HH:mm")
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
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
