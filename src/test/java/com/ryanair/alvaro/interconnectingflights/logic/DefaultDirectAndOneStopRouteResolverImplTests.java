package com.ryanair.alvaro.interconnectingflights.logic;

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

import com.ryanair.alvaro.interconnectingflights.logic.route.DefaultDirectAndOneStopRouteResolverImplService;
import com.ryanair.alvaro.interconnectingflights.model.ResolvedRoute;
import com.ryanair.alvaro.interconnectingflights.model.json.Route;
import com.ryanair.alvaro.interconnectingflights.provider.RouteProvider;

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
	private DefaultDirectAndOneStopRouteResolverImplService resolver = new DefaultDirectAndOneStopRouteResolverImplService();

	@Before
	public void initMocks() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testRoutes_withInterconnectingAirport() {
		Route rightRoute = Route.get("DUB", "MAD", Optional.of("BER"));
		Route otherRoute = Route.get("PZN", "SKP", Optional.of("BUC"));
		when(routeProvider.getRoutes()).thenReturn(Lists.newArrayList(rightRoute, otherRoute));
		List<ResolvedRoute> finalRoute = resolver.resolve("DUB", "MAD");
		assertEquals(0, finalRoute.size());
		finalRoute = resolver.resolve("PZN", "SKP");
		assertEquals(0, finalRoute.size());
	}

	@Test
	public void testRoutes_circularRelation() {
		Route rightRoute = Route.get("DUB", "MAD");
		Route otherRoute = Route.get("MAD", "DUB");
		when(routeProvider.getRoutes()).thenReturn(Lists.newArrayList(rightRoute, otherRoute));
		List<ResolvedRoute> finalRoute = resolver.resolve("DUB", "MAD");
		assertEquals(1, finalRoute.size());
		assertEquals(1, finalRoute.get(0).getRouteConcat().size());
		assertTrue(finalRoute.get(0).getRouteConcat().contains(rightRoute));
	}

	@Test
	public void testRoutes_directRoute() {
		Route rightRoute = Route.get("DUB", "MAD");
		Route otherRoute = Route.get("DUB", "CPH");
		when(routeProvider.getRoutes()).thenReturn(Lists.newArrayList(rightRoute, otherRoute));
		List<ResolvedRoute> finalRoute = resolver.resolve("DUB", "MAD");
		assertEquals(1, finalRoute.size());
		assertEquals(1, finalRoute.get(0).getRouteConcat().size());
		assertTrue(finalRoute.get(0).getRouteConcat().contains(rightRoute));
	}

	@Test
	public void testRoutes_connectingRoute() {
		Route route1 = Route.get("DUB", "MAD");
		Route route2 = Route.get("MAD", "CPH");
		when(routeProvider.getRoutes()).thenReturn(Lists.newArrayList(route1, route2));
		List<ResolvedRoute> finalRoute = resolver.resolve("DUB", "CPH");
		assertEquals(1, finalRoute.size());
		assertEquals(2, finalRoute.get(0).getRouteConcat().size());
		assertTrue(finalRoute.get(0).getRouteConcat().containsAll(Lists.newArrayList(route1, route2)));
	}

	@Test
	public void testRoutes_multipleOptionConnectingRoute() {
		Route route1 = Route.get("DUB", "STN");
		Route route2 = Route.get("STN", "MAD");
		Route route3 = Route.get("MAD", "CPH");
		when(routeProvider.getRoutes()).thenReturn(Lists.newArrayList(route1, route2, route3));
		List<ResolvedRoute> finalRoute = resolver.resolve("DUB", "CPH");
		assertEquals(0, finalRoute.size());
	}

	@Test
	public void testRoutes_destinationNotFound() {
		Route route1 = Route.get("DUB", "STN");
		Route route2 = Route.get("STN", "MAD");
		Route route3 = Route.get("MAD", "CPH");
		when(routeProvider.getRoutes()).thenReturn(Lists.newArrayList(route1, route2, route3));
		List<ResolvedRoute> finalRoute = resolver.resolve("DUB", "BRS");
		assertEquals(0, finalRoute.size());
	}

	@Test
	public void testRoutes_destinationNotLinked() {
		Route route1 = Route.get("DUB", "STN");
		Route route2 = Route.get("STN", "MAD");
		Route route3 = Route.get("DUB", "CPH");
		when(routeProvider.getRoutes()).thenReturn(Lists.newArrayList(route1, route2, route3));
		List<ResolvedRoute> finalRoute = resolver.resolve("DUB", "MAD");
		assertEquals(1, finalRoute.size());
	}

}
