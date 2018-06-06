package com.ryanair.alvaro.interconnectingflights;

import java.util.List;
import java.util.Objects;

import org.assertj.core.util.Lists;

import com.ryanair.alvaro.interconnectingflights.model.Route;

public final class FinalRoute {

	private final List<Route> routeConcat;

	public FinalRoute(Route... routes) {
		Objects.nonNull(routes);
		this.routeConcat = Lists.newArrayList(routes);
	}

	public List<Route> getRouteConcat() {
		return Lists.newArrayList(routeConcat);
	}
}
