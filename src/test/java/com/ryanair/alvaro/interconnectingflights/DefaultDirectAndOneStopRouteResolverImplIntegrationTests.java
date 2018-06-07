package com.ryanair.alvaro.interconnectingflights;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.ryanair.alvaro.interconnectingflights.logic.DefaultDirectAndOneStopRouteResolverImpl;
import com.ryanair.alvaro.interconnectingflights.model.ResolvedRoute;

@RunWith(SpringRunner.class)
@SpringBootTest
@Import(TestConfig.class)
@TestPropertySource("classpath:global.properties")
public class DefaultDirectAndOneStopRouteResolverImplIntegrationTests {

	@Autowired
	private DefaultDirectAndOneStopRouteResolverImpl defaultDirectAndOneStopRouteResolverImpl;

	@Test
	public void testRyanairRouteProvider() {
		List<ResolvedRoute> routes = defaultDirectAndOneStopRouteResolverImpl.resolve("BGY", "NUE");
		assertNotNull(routes);
		assertTrue(routes.size() > 0);
	}

}
