package com.ryanair.alvaro.interconnectingflights.logic;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.ryanair.alvaro.interconnectingflights.model.Route;

/**
 * Class retrieving routes avaialble through the Ryanair API endpoint
 * core/3/routes
 * 
 * @author Alvaro Velasco Fernandez
 *
 */
@Service
public final class RyanairRouteProviderImpl implements RouteProvider {

	@Value("${ryanair.routes.url}")
	private String ENDPOINT_URL;

	private RestTemplate restTemplate;

	@Autowired
	private MessageSource messageSource;

	public RyanairRouteProviderImpl() {
	}

	@Autowired
	public void setRestTemplate(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	@Override
	public List<Route> getRoutes() {
		ResponseEntity<Route[]> responseEntity = restTemplate.getForEntity(ENDPOINT_URL, Route[].class);
		if (!responseEntity.getStatusCode().is2xxSuccessful()) {
			throw new RestClientException(messageSource.getMessage("routeprovider.ryanair.error",
					new String[] { responseEntity.getStatusCode().getReasonPhrase() }, Locale.getDefault()));
		}

		return Arrays.stream(responseEntity.getBody()).collect(Collectors.toList());
	}

}
