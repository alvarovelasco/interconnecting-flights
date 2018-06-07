package com.ryanair.alvaro.interconnectingflights.logic;

import java.util.List;

import com.ryanair.alvaro.interconnectingflights.model.ResolvedRoute;

/**
 * Interface providing a method for resolving the possible routes between two airports
 * @author Alvaro Velasco Fernandez
 *
 */
public interface RouteResolver {

	List<ResolvedRoute> resolve(String expectedOrigin, String expectedDestination);

}
