package com.ryanair.alvaro.interconnectingflights.model.json;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ResolvedSchedule implements Comparable<ResolvedSchedule> {

	@JsonProperty("stops")
	private int stops;

	@JsonProperty("legs")
	private List<LegFlight> flights;

	private ResolvedSchedule(LegFlight... flights) {
		this.flights = Arrays.asList(Objects.requireNonNull(flights)).stream().filter(s -> s != null)
				.collect(Collectors.toList());
		this.stops = this.flights.size() - 1;
	}
	
	@Override
	public int compareTo(ResolvedSchedule otherSchedule) {
		if (stops < otherSchedule.stops)
			return -1;
		else if (stops > otherSchedule.stops)
			return 1;
		return 0;
	}

	@Override
	public String toString() {
		return "ResolvedSchedule [stops=" + stops + ", flights=" + flights + "]";
	}

	public static class Builder {

		private LegFlight firstRoute;

		private Optional<LegFlight> secondRoute = Optional.empty();

		private final int expectedStops;

		public Builder(int expectedStops) {
			this.expectedStops = expectedStops;
		}

		public Builder reset() {
			this.firstRoute = null;
			this.secondRoute = Optional.empty();
			return this;
		}

		public Builder addFirstRoute(Route route, LocalDateTime departure, LocalDateTime arrival) {
			Objects.requireNonNull(route);
			this.firstRoute = new LegFlight(route.getOrigin(), route.getDestination(), departure, arrival);
			return this;
		}

		public Builder addSecondRoute(Route route, LocalDateTime departure, LocalDateTime arrival) {
			Objects.requireNonNull(route);
			this.secondRoute = Optional
					.of(new LegFlight(route.getOrigin(), route.getDestination(), departure, arrival));
			return this;
		}

		public Optional<ResolvedSchedule> build() {
			if (expectedStops == 1 && !secondRoute.isPresent())
				return Optional.empty();

			return Optional.of(new ResolvedSchedule(firstRoute, secondRoute.orElse(null)));
		}
	}

}
