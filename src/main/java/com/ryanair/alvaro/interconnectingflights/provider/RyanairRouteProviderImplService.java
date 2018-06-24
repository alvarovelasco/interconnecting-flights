package com.ryanair.alvaro.interconnectingflights.provider;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.ryanair.alvaro.interconnectingflights.model.json.Route;

/**
 * Class retrieving routes avaialble through the Ryanair API endpoint
 * core/3/routes
 * 
 * @author Alvaro Velasco Fernandez
 *
 */
@Repository
public final class RyanairRouteProviderImplService implements RouteProvider {

	@Value("${ryanair.routes.url}")
	private String enpointUrl;

	private RestTemplate restTemplate;

	@Autowired
	private MessageSource messageSource;

	public RyanairRouteProviderImplService() {
	}

	@Autowired
	public void setRestTemplate(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	@Override
	public List<Route> getRoutes() {
		ResponseEntity<Route[]> responseEntity = restTemplate.getForEntity(enpointUrl, Route[].class);
		if (!responseEntity.getStatusCode().is2xxSuccessful()) {
			throw new RestClientException(messageSource.getMessage("routeprovider.ryanair.error",
					new String[] { responseEntity.getStatusCode().getReasonPhrase() }, Locale.getDefault()));
		}

		return Arrays.stream(responseEntity.getBody()).collect(Collectors.toList());
	}

}
