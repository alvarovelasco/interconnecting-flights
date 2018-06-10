package com.ryanair.alvaro.interconnectingflights.provider;

import java.util.List;

import com.ryanair.alvaro.interconnectingflights.model.json.Route;

public interface RouteProvider {
	List<Route> getRoutes();
}
