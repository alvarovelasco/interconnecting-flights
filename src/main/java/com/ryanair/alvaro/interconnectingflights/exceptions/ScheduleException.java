package com.ryanair.alvaro.interconnectingflights.exceptions;

public class ScheduleException extends RuntimeException {

	public ScheduleException(String message) {
		super(message);
	}

	public ScheduleException(String message, Throwable t) {
		super(message, t);
	}
}
