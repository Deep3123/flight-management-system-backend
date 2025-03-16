package com.flight.management.service;

import java.util.List;

import com.flight.management.proxy.FlightProxy;

<<<<<<< HEAD
import jakarta.validation.Valid;

=======
>>>>>>> c8b7e423bf58468af87d988072869ce6d2389aff
public interface FlightService {
	public String addFlightDetails(FlightProxy flightProxy);

	public List<FlightProxy> getAllFlightsDetails();

	public FlightProxy getFlightDetailsByFlightNumber(String flightNumber);

	public String updateFlightDetails(FlightProxy flightProxy);

<<<<<<< HEAD
	public String deleteFlightDetails(String flightNumber);

	public List<FlightProxy> getFlightDetailsByDepartureAndArrival(String departure, String Arrival);
=======
	public String deleteFlightDetails(Long flightNumber);
>>>>>>> c8b7e423bf58468af87d988072869ce6d2389aff
}
