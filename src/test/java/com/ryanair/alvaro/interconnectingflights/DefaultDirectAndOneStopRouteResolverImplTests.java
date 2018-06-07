package com.ryanair.alvaro.interconnectingflights;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import com.ryanair.alvaro.interconnectingflights.logic.DefaultDirectAndOneStopRouteResolverImpl;
import com.ryanair.alvaro.interconnectingflights.logic.RouteProvider;
import com.ryanair.alvaro.interconnectingflights.model.FinalRoute;
import com.ryanair.alvaro.interconnectingflights.model.Route;

/**
 * Unit testing DefaultDirectAndStopRouteResolver class in user cases
 * 
 * @author Alvaro Velasco Fernandez
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class DefaultDirectAndOneStopRouteResolverImplTests {

	@Mock
	private RouteProvider routeProvider;

	@InjectMocks
	private DefaultDirectAndOneStopRouteResolverImpl resolver = new DefaultDirectAndOneStopRouteResolverImpl();

	@Before
	public void initMocks() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testRoutes_withInterconnectingAirport() {
		Route rightRoute = Route.get("DUB", "MAD", Optional.of("BER"));
		Route otherRoute = Route.get("PZN", "SKP", Optional.of("BUC"));
		when(routeProvider.getRoutes()).thenReturn(Lists.newArrayList(rightRoute, otherRoute));
		List<FinalRoute> finalRoute = resolver.resolve("DUB", "MAD");
		assertEquals(0, finalRoute.size());
		finalRoute = resolver.resolve("PZN", "SKP");
		assertEquals(0, finalRoute.size());
	}

	@Test
	public void testRoutes_circularRelation() {
		Route rightRoute = Route.get("DUB", "MAD", Optional.empty());
		Route otherRoute = Route.get("MAD", "DUB", Optional.empty());
		when(routeProvider.getRoutes()).thenReturn(Lists.newArrayList(rightRoute, otherRoute));
		List<FinalRoute> finalRoute = resolver.resolve("DUB", "MAD");
		assertEquals(1, finalRoute.size());
		assertEquals(1, finalRoute.get(0).getRouteConcat().size());
		assertTrue(finalRoute.get(0).getRouteConcat().contains(rightRoute));
	}

	@Test
	public void testRoutes_directRoute() {
		Route rightRoute = Route.get("DUB", "MAD", Optional.empty());
		Route otherRoute = Route.get("DUB", "CPH", Optional.empty());
		when(routeProvider.getRoutes()).thenReturn(Lists.newArrayList(rightRoute, otherRoute));
		List<FinalRoute> finalRoute = resolver.resolve("DUB", "MAD");
		assertEquals(1, finalRoute.size());
		assertEquals(1, finalRoute.get(0).getRouteConcat().size());
		assertTrue(finalRoute.get(0).getRouteConcat().contains(rightRoute));
	}

	@Test
	public void testRoutes_connectingRoute() {
		Route route1 = Route.get("DUB", "MAD", Optional.empty());
		Route route2 = Route.get("MAD", "CPH", Optional.empty());
		when(routeProvider.getRoutes()).thenReturn(Lists.newArrayList(route1, route2));
		List<FinalRoute> finalRoute = resolver.resolve("DUB", "CPH");
		assertEquals(1, finalRoute.size());
		assertEquals(2, finalRoute.get(0).getRouteConcat().size());
		assertTrue(finalRoute.get(0).getRouteConcat().containsAll(Lists.newArrayList(route1, route2)));
	}

	@Test
	public void testRoutes_multipleOptionConnectingRoute() {
		Route route1 = Route.get("DUB", "STN", Optional.empty());
		Route route2 = Route.get("STN", "MAD", Optional.empty());
		Route route3 = Route.get("MAD", "CPH", Optional.empty());
		when(routeProvider.getRoutes()).thenReturn(Lists.newArrayList(route1, route2, route3));
		List<FinalRoute> finalRoute = resolver.resolve("DUB", "CPH");
		assertEquals(0, finalRoute.size());
	}

	@Test
	public void testRoutes_destinationNotFound() {
		Route route1 = Route.get("DUB", "STN", Optional.empty());
		Route route2 = Route.get("STN", "MAD", Optional.empty());
		Route route3 = Route.get("MAD", "CPH", Optional.empty());
		when(routeProvider.getRoutes()).thenReturn(Lists.newArrayList(route1, route2, route3));
		List<FinalRoute> finalRoute = resolver.resolve("DUB", "BRS");
		assertEquals(0, finalRoute.size());
	}

}
