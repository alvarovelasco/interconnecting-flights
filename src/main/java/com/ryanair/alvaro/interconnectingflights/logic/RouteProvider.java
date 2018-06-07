package com.ryanair.alvaro.interconnectingflights.logic;

import java.util.List;

import com.ryanair.alvaro.interconnectingflights.model.Route;

public interface RouteProvider {
	List<Route> getRoutes();
}
