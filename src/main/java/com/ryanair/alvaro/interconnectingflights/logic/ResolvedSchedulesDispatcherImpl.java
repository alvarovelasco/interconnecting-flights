package com.ryanair.alvaro.interconnectingflights.logic;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.ryanair.alvaro.interconnectingflights.model.ResolvedRoute;
import com.ryanair.alvaro.interconnectingflights.model.ScheduledFlightAndRouteMap;

@Service
public class ResolvedSchedulesDispatcherImpl implements ResolvedSchedulesDispatcher {

	@Resource(name = "nonStopScheduleResolver")
	private ScheduleResolver nonStopScheduleResolverImpl;
	
	@Resource(name = "oneStopScheduleResolver")
	private ScheduleResolver oneStopScheduleResolverImpl;
	
	public void set(ScheduledFlightAndRouteMap scheduledFlightAndRouteMap) {
		this.nonStopScheduleResolverImpl.set(scheduledFlightAndRouteMap);
		this.oneStopScheduleResolverImpl.set(scheduledFlightAndRouteMap);
	}
	
	@Override
	public ScheduleResolver dispatch(ResolvedRoute resolvedRoute) {
		if (resolvedRoute.getRouteConcat().size() == 1) {
			return nonStopScheduleResolverImpl;
		} else {
			return oneStopScheduleResolverImpl;
		}
	}
	

}
