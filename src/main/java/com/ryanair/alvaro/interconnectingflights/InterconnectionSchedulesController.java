package com.ryanair.alvaro.interconnectingflights;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.ryanair.alvaro.interconnectingflights.logic.ScheduleAbsoluteRouteInterconnectionResolverImplService;
import com.ryanair.alvaro.interconnectingflights.model.json.ResolvedSchedule;
import com.ryanair.alvaro.interconnectingflights.model.json.Route;

@RestController
public class InterconnectionSchedulesController {

	@Autowired
	private ScheduleAbsoluteRouteInterconnectionResolverImplService interconnectionResolver;
	
	@RequestMapping(name="/interconnections", method=RequestMethod.GET,produces = "application/json")
    public @ResponseBody String interconnections(@RequestParam(value="departure", defaultValue="") String departure
    		,@RequestParam("arrival") String arrival, @RequestParam("departureDateTime")@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime departureTime,
    		@RequestParam("arrivalDateTime") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime arrivalDateTime) throws JsonProcessingException {
		
		List<ResolvedSchedule> resolvedSchedules = 
				interconnectionResolver.resolve(Route.get(departure, arrival), departureTime, arrivalDateTime);
		
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);

		String formattedJsonResolvedSchedules 
				= mapper.writerWithDefaultPrettyPrinter().writeValueAsString(resolvedSchedules);
		return formattedJsonResolvedSchedules;
    }
}
