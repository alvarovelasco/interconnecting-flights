package com.ryanair.alvaro.interconnectingflights;

import java.util.List;

import com.ryanair.alvaro.interconnectingflights.model.Route;

public interface RouteProvider {
	List<Route> getRoutes();
}
