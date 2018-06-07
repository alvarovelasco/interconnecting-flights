package com.ryanair.alvaro.interconnectingflights.logic;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ryanair.alvaro.interconnectingflights.exceptions.RouteNotFoundException;
import com.ryanair.alvaro.interconnectingflights.model.ResolvedRoute;
import com.ryanair.alvaro.interconnectingflights.model.Route;

/**
 * 
 * Resolve the routes between two airports regarding a set of rules that must comply:
 * 		<ul>The route must not have interconnecting airport
 * 		<ul>The route may be direct or
 * 		<ul>The route may have a third airport connecting the origin and destination.
 * 
 * @author Alvaro Velasco Fernandez
 *
 */
@Service
public class DefaultDirectAndOneStopRouteResolverImpl implements RouteResolver {
	
	@Autowired
	private RouteProvider routeProvider;
	
	
	@Override
	public List<ResolvedRoute> resolve(String expectedOrigin, String expectedDestination) {
		requireNonNull(expectedOrigin);
		requireNonNull(expectedDestination);

		// 1. retrieve
		List<Route> routes = routeProvider.getRoutes();

		// 2. Filter routes containing origin airport or destination airport and
		// filter routes with interconnectingAirport
		routes = routes.stream()
				.filter(r -> r.getOrigin().equals(expectedOrigin) || r.getDestination().equals(expectedDestination))
				.filter(Route::noConnectingAirport).collect(Collectors.toList());

		// 3. group the routes and concatenate them in case of one stop.
		List<ResolvedRoute> finalRoutes = routes.stream()
				.map(getTransformRoutesIntoFinalRoutesFunction(expectedOrigin, expectedDestination, routes))
				.filter(Objects::nonNull).collect(Collectors.toList());

		return finalRoutes;
	}

	private Function<Route, ResolvedRoute> getTransformRoutesIntoFinalRoutesFunction(final String expectedOrigin,
			final String expectedDestination, final List<Route> routes) {
		return (route) -> {
			// Case with unique route
			if (route.getOrigin().equals(expectedOrigin)) {
				if (route.getDestination().equals(expectedDestination)) {
					return new ResolvedRoute(route);
				} else {
					// Find the next route
					Route theOtherRoute = routes.stream().filter(
							r -> route.getDestination().equals(r.getOrigin()))
													.findFirst().orElse(null);
					if (theOtherRoute != null)
						return new ResolvedRoute(route, theOtherRoute);
				}
			}

			return null;
		};
	}

}
