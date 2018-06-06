package com.ryanair.alvaro.interconnectingflights;

import java.util.List;

/**
 * Interface providing a method for resolving the possible routes between two airports
 * @author Alvaro Velasco Fernandez
 *
 */
public interface RouteResolver {

	List<FinalRoute> resolve(String expectedOrigin, String expectedDestination);

}
