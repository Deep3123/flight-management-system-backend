package com.flight.management.service;

import java.util.List;

import com.flight.management.proxy.FlightProxy;

import jakarta.validation.Valid;

public interface FlightService {
	public String addFlightDetails(FlightProxy flightProxy);

	public List<FlightProxy> getAllFlightsDetails();

	public FlightProxy getFlightDetailsByFlightNumber(String flightNumber);

	public String updateFlightDetails(FlightProxy flightProxy);

	public String deleteFlightDetails(String flightNumber);

	public List<FlightProxy> getFlightDetailsByDepartureAndArrival(String departure, String Arrival);
}
