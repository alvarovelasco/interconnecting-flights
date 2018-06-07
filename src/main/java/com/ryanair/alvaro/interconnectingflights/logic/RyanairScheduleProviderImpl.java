package com.ryanair.alvaro.interconnectingflights.logic;

import static java.util.Objects.requireNonNull;

import java.time.YearMonth;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.ryanair.alvaro.interconnectingflights.model.Route;
import com.ryanair.alvaro.interconnectingflights.model.ScheduledMonthFlight;

@Service
public final class RyanairScheduleProviderImpl implements ScheduleProvider {
	
	@Value("${ryanair.schedules.url}")
	private String enpointUrl;

	private RestTemplate restTemplate;

	@Autowired
	private MessageSource messageSource;


	@Autowired
	public void setRestTemplate(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	@Override
	public ScheduledMonthFlight getFlights(Route route, YearMonth yearMonth) {
		requireNonNull(route);
		requireNonNull(yearMonth);
		
		Map<String, String> parameters = new HashMap<>();
		parameters.put("departure", route.getOrigin());
		parameters.put("arrival", route.getDestination());
		parameters.put("year", String.valueOf(yearMonth.getYear()));
		parameters.put("month", String.valueOf(yearMonth.getMonthValue()));
		
		ResponseEntity<ScheduledMonthFlight> responseEntity = 
				restTemplate.getForEntity(enpointUrl, ScheduledMonthFlight.class, parameters);
		
		if (!responseEntity.getStatusCode().is2xxSuccessful()) {
			throw new RestClientException(messageSource.getMessage("schedule.ryanair.error",
					new String[] { responseEntity.getStatusCode().getReasonPhrase() }, Locale.getDefault()));
		}
		
		return responseEntity.getBody();
	}

}
