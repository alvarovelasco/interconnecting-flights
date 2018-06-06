package com.ryanair.alvaro.interconnectingflights;

import static org.mockito.Mockito.when;

import java.io.File;
import java.util.List;

import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import com.ryanair.alvaro.interconnectingflights.model.Route;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RouteFilterTests {

	@Mock
	private RouteRetriever routeRetrieverMock;

	@Before
	public void initMocks() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testRoutesWithSomeInterconnectingAirport() {
		initRouteRetrieverMock("interconnectingAirports.json");
		RouteResolver resolver = new DefaultDirectAndOneStopRouteResolverImpl();
		// resolver.resolve(, );
	}

	private void initRouteRetrieverMock(String file) {
		when(routeRetrieverMock.getRoutes()).thenAnswer(new Answer<List<Route>>() {
			@Override
			public List<Route> answer(InvocationOnMock arg0) throws Throwable {
				Route[] routes = new RestTemplate().getForObject(new File(file).toURI(), Route[].class);
				return Lists.newArrayList(routes);
			}
		});
	}

}
