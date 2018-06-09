package com.ryanair.alvaro.interconnectingflights.model.json;

import java.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ScheduledDayFlight {

	@JsonProperty("departureTime")
	@JsonFormat(pattern = "HH:mm")
	private LocalTime departureTime;
	
	@JsonProperty("arrivalTime")
	@JsonFormat(pattern = "HH:mm")
	private LocalTime arrivalTime;
	
	public LocalTime getArrivalTime() {
		return arrivalTime;
	}
	
	public LocalTime getDepartureTime() {
		return departureTime;
	}

	@Override
	public String toString() {
		return "ScheduledDayPlanning [departureTime=" + departureTime + ", arrivalTime=" + arrivalTime + "]";
	}
	
	
}
