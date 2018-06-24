package com.ryanair.alvaro.interconnectingflights.provider;

import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ryanair.alvaro.interconnectingflights.exceptions.ScheduleException;
import com.ryanair.alvaro.interconnectingflights.model.json.Route;
import com.ryanair.alvaro.interconnectingflights.model.json.ScheduledMonthFlight;

@Repository
public final class RyanairScheduleProviderImplService implements ScheduleProvider {

	@Value("${ryanair.schedules.url}")
	private String endpointUrl;

	private RestTemplate restTemplate;

	@Autowired
	private ObjectMapper objectMapper;


	@Autowired
	public void setRestTemplate(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	@Override
	public Optional<ScheduledMonthFlight> getFlights(Route route, YearMonth yearMonth) {
		requireNonNull(route);
		requireNonNull(yearMonth);

		Map<String, String> parameters = new HashMap<>();
		parameters.put("departure", route.getOrigin());
		parameters.put("arrival", route.getDestination());
		parameters.put("year", String.valueOf(yearMonth.getYear()));
		parameters.put("month", String.valueOf(yearMonth.getMonthValue()));

		HttpHeaders headers = new HttpHeaders();
		headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);

		HttpEntity<String> request = new HttpEntity<String>(headers);
		try {
			ResponseEntity<String> response = restTemplate.exchange(endpointUrl, HttpMethod.GET, request, String.class,
					parameters);
			String responseBody = response.getBody();

			if (HttpStatus.Series.CLIENT_ERROR.equals(response.getStatusCode())) {
				return Optional.empty();
			}

			ScheduledMonthFlight scheduledMonthFlight = objectMapper.readValue(responseBody,
					ScheduledMonthFlight.class);
			return Optional.of(scheduledMonthFlight);

		} catch (HttpClientErrorException ex) {
			return Optional.empty();
		} catch (JsonParseException e) {
			e.printStackTrace();
			throw new ScheduleException("parse exception", e);
		} catch (JsonMappingException e) {
			e.printStackTrace();
			throw new ScheduleException("mapping exception", e);
		} catch (IOException e) {
			e.printStackTrace();
			throw new ScheduleException("other exception", e);
		}

	}

}
