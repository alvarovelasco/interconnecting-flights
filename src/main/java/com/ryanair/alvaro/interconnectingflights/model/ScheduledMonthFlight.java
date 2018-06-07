package com.ryanair.alvaro.interconnectingflights.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ScheduledMonthFlight {

	@JsonProperty("days")
	private List<FullScheduledDay> days;
	
	public List<FullScheduledDay> getDays() {
		return days;
	}
	
	public static class FullScheduledDay {
		
		@JsonProperty("day")
		private int day;
		
		@JsonProperty("flights")
		private List<ScheduledDayFlight> flights;
		
		public int getDay() {
			return day;
		}
		
		public List<ScheduledDayFlight> getFlights() {
			return flights;
		}
	}
}
