package com.ryanair.alvaro.interconnectingflights;

import java.util.List;

import com.ryanair.alvaro.interconnectingflights.model.Route;

// TODO AVF: Implementation must have @Repository annotation
public interface RouteRetriever {
	List<Route> getRoutes();
}
