package com.ryanair.alvaro.interconnectingflights;

import java.util.List;

public interface RouteResolver {

	List<FinalRoute> resolve(String expectedOrigin, String expectedDestination);

}
