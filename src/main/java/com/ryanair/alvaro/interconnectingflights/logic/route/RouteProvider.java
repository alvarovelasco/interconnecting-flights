package com.ryanair.alvaro.interconnectingflights.logic.route;

import java.util.List;

import com.ryanair.alvaro.interconnectingflights.model.json.Route;

public interface RouteProvider {
	List<Route> getRoutes();
}
