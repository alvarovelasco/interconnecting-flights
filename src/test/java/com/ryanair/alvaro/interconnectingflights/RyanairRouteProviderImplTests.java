package com.ryanair.alvaro.interconnectingflights;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.ryanair.alvaro.interconnectingflights.logic.RouteProvider;
import com.ryanair.alvaro.interconnectingflights.logic.RyanairRouteProviderImpl;
import com.ryanair.alvaro.interconnectingflights.model.Route;

/**
 * Unit test class of RyanairRouteProviderImpl class.
 * 
 * @author Alvaro Velasco Fernandez
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Import(TestConfig.class)
@TestPropertySource("classpath:global.properties")
public class RyanairRouteProviderImplTests {

	@Mock
	private RouteProvider routeProvider;

	@Mock
	private RestTemplate mockTemplate;

	@Autowired
	@InjectMocks
	private RyanairRouteProviderImpl providerImpl;

	@Autowired
	RestTemplate restTemplate;

	@Test
	public void testRyanairRouteProvider() {
		List<Route> routes = providerImpl.getRoutes();
		assertNotNull(routes);
		assertTrue(routes.size() > 0);
	}

	@Test(expected = RestClientException.class)
	public void testRyanairRouteProviderFailure() {
		ResponseEntity mockResponseEntity = mock(ResponseEntity.class);
		when(mockResponseEntity.getStatusCode()).thenReturn(HttpStatus.NOT_FOUND);
		when(mockTemplate.getForEntity(Mockito.anyString(), Mockito.any())).thenReturn(mockResponseEntity);

		providerImpl.setRestTemplate(mockTemplate);

		providerImpl.getRoutes();
	}

	@After
	public void resetProvider() {
		providerImpl.setRestTemplate(restTemplate);
	}

}
