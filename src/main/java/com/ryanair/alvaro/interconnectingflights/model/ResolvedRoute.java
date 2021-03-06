package com.ryanair.alvaro.interconnectingflights.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import com.ryanair.alvaro.interconnectingflights.model.json.Route;

public final class ResolvedRoute {

	private final List<Route> routeConcat;

	public ResolvedRoute(Route... routes) {
		Objects.nonNull(routes);
		this.routeConcat = Arrays.asList(routes);
	}

	public List<Route> getRouteConcat() {
		return new ArrayList<>(routeConcat);
	}
	

	@Override
	public String toString() {
		return "ResolvedRoute [routeConcat=" + routeConcat + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((routeConcat == null) ? 0 : routeConcat.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ResolvedRoute other = (ResolvedRoute) obj;
		if (routeConcat == null) {
			if (other.routeConcat != null)
				return false;
		} else if (!routeConcat.equals(other.routeConcat))
			return false;
		return true;
	}

}
