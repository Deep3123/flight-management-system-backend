package com.flight.management.service.impl;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.flight.management.domain.FlightEntity;
import com.flight.management.proxy.FlightProxy;
import com.flight.management.proxy.FlightSearchProxy;
import com.flight.management.repo.FlightRepo;
import com.flight.management.service.FlightService;
import com.flight.management.util.MapperUtil;

@Service
public class FlightServiceImpl implements FlightService {

	@Autowired
	private FlightRepo repo;

//	@Override
//	public String addFlightDetails(FlightProxy flightProxy) {
//		// TODO Auto-generated method stub
//		Optional<FlightEntity> flight = repo.findByFlightNumber(flightProxy.getFlightNumber());
//
//		if (flight.isEmpty() || flight.get() == null) {
//			flight.get().setCreatedAt(new Date());
//			flight.get().setUpdatedAt(new Date());
//
//			repo.save(MapperUtil.convertValue(flightProxy, FlightEntity.class));
//			return "Flight data saved successfully!!";
//		}
//
//		return "Flight already exist with given flight number.";
//	}

	@Override
	public String addFlightDetails(FlightProxy flightProxy) {
		// Fetch the flight based on flight number
		Optional<FlightEntity> flight = repo.findByFlightNumber(flightProxy.getFlightNumber());

		// Check if the flight does not exist (empty Optional)
		if (flight.isEmpty()) {
			// Create a new FlightEntity and set the fields
			FlightEntity newFlight = MapperUtil.convertValue(flightProxy, FlightEntity.class);
			newFlight.setCreatedAt(new Date());
			newFlight.setUpdatedAt(new Date());

			// Save the new flight
			repo.save(newFlight);
			return "Flight data saved successfully!!";
		}

		return "Flight already exists with the given flight number.";
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
			if (flightProxy.getDepartureDate() != null)
				flight.get().setDepartureDate(flightProxy.getDepartureDate());

			if (flightProxy.getDepartureTime() != null)
				flight.get().setDepartureTime(flightProxy.getDepartureTime());

			if (flightProxy.getArrivalDate() != null)
				flight.get().setArrivalDate(flightProxy.getArrivalDate());

			if (flightProxy.getArrivalTime() != null)
				flight.get().setArrivalTime(flightProxy.getArrivalTime());

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

//			if (flightProxy.getAirlineName() != null)
//				flight.get().setAirlineName(flightProxy.getAirlineName());

			if (flightProxy.getFlightClass() != null)
				flight.get().setFlightClass(flightProxy.getFlightClass());

			flight.get().setUpdatedAt(new Date());

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
	public List<FlightProxy> getFlightDetailsByUserDetails(FlightSearchProxy flightSearchProxy) {
		// TODO Auto-generated method stub
//		System.err.println(flightSearchProxy);
//		Optional<List<FlightEntity>> flightList = repo
//				.findByDepartureAirportAndArrivalAirportAndDepartureDateAndArrivalDateAndFlightClass(
//						flightSearchProxy.getDepartureAirport(), flightSearchProxy.getArrivalAirport(),
//						flightSearchProxy.getDepartureDate(), flightSearchProxy.getArrivalDate(),
//						flightSearchProxy.getFlightClass());
//		System.err.println(flightList.get());
//		if (flightList.isPresent()) {
//			return MapperUtil.convertListofValue(flightList.get().stream()
//					.filter(obj -> obj.getSeatsAvailable() >= flightSearchProxy.getPersonCount())
//					.collect(Collectors.toList()), FlightProxy.class);
//		}
//		return null;

//		System.err.println(flightSearchProxy);
//
//		List<FlightEntity> flightList = repo
//				.findByDepartureAirportAndArrivalAirportAndDepartureDateAndArrivalDateAndFlightClass(
//						flightSearchProxy.getDepartureAirport(), flightSearchProxy.getArrivalAirport(),
//						flightSearchProxy.getDepartureDate(), flightSearchProxy.getArrivalDate(),
//						flightSearchProxy.getFlightClass());
//
//		if (!flightList.isEmpty()) {
//			System.err.println(flightList);
//			return MapperUtil.convertListofValue(
//					flightList.stream().filter(obj -> obj.getSeatsAvailable() >= flightSearchProxy.getPersonCount())
//							.collect(Collectors.toList()),
//					FlightProxy.class);
//		}
//		return null;

//		System.err.println(flightSearchProxy);

		// Convert java.util.Date to LocalDate
		LocalDate depLocalDate = flightSearchProxy.getDepartureDate().toInstant().atZone(ZoneId.systemDefault())
				.toLocalDate();

		LocalDate arrLocalDate = flightSearchProxy.getArrivalDate().toInstant().atZone(ZoneId.systemDefault())
				.toLocalDate();

		// Create start and end of the day for both departure and arrival dates
		Date depStart = Date.from(depLocalDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
		Date depEnd = Date.from(depLocalDate.atTime(23, 59, 59).atZone(ZoneId.systemDefault()).toInstant());

		Date arrStart = Date.from(arrLocalDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
		Date arrEnd = Date.from(arrLocalDate.atTime(23, 59, 59).atZone(ZoneId.systemDefault()).toInstant());

		// Use a custom query in the repository with $gte and $lte
		List<FlightEntity> flightList = repo.findFlightsInRange(flightSearchProxy.getDepartureAirport(),
				flightSearchProxy.getArrivalAirport(), depStart, depEnd, arrStart, arrEnd,
				flightSearchProxy.getFlightClass());

		if (!flightList.isEmpty()) {
//			System.err.println(flightList);
			return MapperUtil.convertListofValue(
					flightList.stream().filter(obj -> obj.getSeatsAvailable() >= flightSearchProxy.getPersonCount())
							.collect(Collectors.toList()),
					FlightProxy.class);
		}
		return null;

	}
}
