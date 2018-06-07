package com.ryanair.alvaro.interconnectingflights.logic;

import java.util.List;

import org.assertj.core.util.Lists;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.ryanair.alvaro.interconnectingflights.model.Route;

/**
 * Class retrieving routes avaialble through the Ryanair API endpoint core/3/routes
 * 
 * @author Alvaro Velasco Fernandez
 *
 */
@Component
public final class RyanairRouteProviderImpl implements RouteProvider {

	private static final String ENDPOINT_URL = "https://api.ryanair.com/core/3/routes";
	
	@Override
	public List<Route> getRoutes() {
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<Route[]> responseEntity = restTemplate.getForEntity(ENDPOINT_URL, Route[].class);
		if (!responseEntity.getStatusCode().is2xxSuccessful()) {
			throw new RestClientException("Ryanair route API not reached for some reason: "
						+ responseEntity.getStatusCode().getReasonPhrase());
		}
			
		return Lists.newArrayList(responseEntity.getBody());
	}

}
