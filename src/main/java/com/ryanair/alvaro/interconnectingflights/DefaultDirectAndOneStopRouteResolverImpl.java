package com.ryanair.alvaro.interconnectingflights;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ryanair.alvaro.interconnectingflights.exceptions.RouteNotFoundException;
import com.ryanair.alvaro.interconnectingflights.model.Route;

/**
 * 
 * Resolve the routes
 * 
 * @author Alvaro Velasco Fernandez
 *
 */
@Service
public class DefaultDirectAndOneStopRouteResolverImpl implements RouteResolver {

	// TODO AVF: Add the name from the implementation of RouteResolver
	@Autowired
	private RouteRetriever routeRetriever;

	// TODO AVF: Use a resource for replacing text strings

	@Override
	public List<FinalRoute> resolve(String expectedOrigin, String expectedDestination) {
		requireNonNull(expectedOrigin);
		requireNonNull(expectedDestination);

		// 1. retrieve
		List<Route> routes = routeRetriever.getRoutes();

		// 2. Filter routes containing origin airport or destination airport and
		// filter routes with interconnectingAirport
		routes = routes.stream()
				.filter(r -> r.getOrigin().equals(expectedOrigin) || r.getDestination().equals(expectedDestination))
				.filter(Route::noConnectingAirport).collect(Collectors.toList());

		// 3. group the routes and concatenate them in case of one stop.
		List<FinalRoute> finalRoutes = routes.stream()
				.map(getTransformRoutesIntoFinalRoutesFunction(expectedOrigin, expectedDestination, routes))
				.filter(Objects::nonNull).collect(Collectors.toList());

		return finalRoutes;
	}

	private Function<Route, FinalRoute> getTransformRoutesIntoFinalRoutesFunction(final String expectedOrigin,
			final String expectedDestination, final List<Route> routes) {
		return (route) -> {
			// Case with unique route
			if (route.getOrigin().equals(expectedOrigin)) {
				if (route.getDestination().equals(expectedDestination)) {
					return new FinalRoute(route);
				} else {
					// Find the next route
					Route theOtherRoute = routes.stream().filter(r -> route.getDestination().equals(r.getOrigin()))
							.findFirst().orElseThrow(() -> new RouteNotFoundException());
					return new FinalRoute(route, theOtherRoute);
				}
			}

			return null;
		};
	}

}
