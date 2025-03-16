package com.flight.management.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.flight.management.domain.FlightEntity;
import com.flight.management.proxy.FlightProxy;
import com.flight.management.repo.FlightRepo;
import com.flight.management.service.FlightService;
import com.flight.management.util.MapperUtil;

@Service
public class FlightServiceImpl implements FlightService {

	@Autowired
	private FlightRepo repo;

	@Override
	public String addFlightDetails(FlightProxy flightProxy) {
		// TODO Auto-generated method stub
		repo.save(MapperUtil.convertValue(flightProxy, FlightEntity.class));
		return "Flight data saved successfully!!";
	}

	@Override
	public List<FlightProxy> getAllFlightsDetails() {
		// TODO Auto-generated method stub
		return MapperUtil.convertListofValue(repo.findAll(), FlightProxy.class);
	}

	@Override
	public FlightProxy getFlightDetailsByFlightNumber(String flightNumber) {
		// TODO Auto-generated method stub
		Optional<FlightEntity> flight = repo.findByFlightNumber(flightNumber);

		if (flight.isPresent())
			return MapperUtil.convertValue(flight.get(), FlightProxy.class);

		return null;
	}

	@Override
	public String updateFlightDetails(FlightProxy flightProxy) {
		Optional<FlightEntity> flight = repo.findByFlightNumber(flightProxy.getFlightNumber());

		if (flight.isPresent()) {
			// Update flight details if not null in the FlightProxy
			if (flightProxy.getDepartureDateAndTime() != null)
				flight.get().setDepartureDateAndTime(flightProxy.getDepartureDateAndTime());

			if (flightProxy.getArrivalDateAndTime() != null)
				flight.get().setArrivalDateAndTime(flightProxy.getArrivalDateAndTime());

			if (flightProxy.getDepartureAirport() != null)
				flight.get().setDepartureAirport(flightProxy.getDepartureAirport());

			if (flightProxy.getArrivalAirport() != null)
				flight.get().setArrivalAirport(flightProxy.getArrivalAirport());

			if (flightProxy.getPrice() != null)
				flight.get().setPrice(flightProxy.getPrice());

			if (flightProxy.getSeatsAvailable() != null)
				flight.get().setSeatsAvailable(flightProxy.getSeatsAvailable());

			if (flightProxy.getDurationMinutes() != null)
				flight.get().setDurationMinutes(flightProxy.getDurationMinutes());

			if (flightProxy.getAirlineName() != null)
				flight.get().setAirlineName(flightProxy.getAirlineName());

			// Save the updated flight
			repo.save(flight.get());

			return "Flight details updated successfully.";
		}

		return "Flight not found.";
	}

	@Override
	public String deleteFlightDetails(String flightNumber) {
		// TODO Auto-generated method stub
		Optional<FlightEntity> flight = repo.findByFlightNumber(flightNumber);

		if (flight.isPresent()) {
			repo.delete(flight.get());
			return "Flight record deleted successsfully.";
		}
		return null;
	}

	@Override
	public List<FlightProxy> getFlightDetailsByDepartureAndArrival(String departure, String arrival) {
		// TODO Auto-generated method stub
		Optional<List<FlightEntity>> flightList = repo.findByDepartureAirportAndArrivalAirport(departure, arrival);

		if (flightList.isPresent()) {
			return MapperUtil.convertListofValue(flightList.get(), FlightProxy.class);
		}
		return null;
	}

}
