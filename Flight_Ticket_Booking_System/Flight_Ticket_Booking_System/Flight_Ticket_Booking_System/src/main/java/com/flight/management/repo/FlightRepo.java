package com.flight.management.repo;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.flight.management.domain.FlightEntity;

public interface FlightRepo extends JpaRepository<FlightEntity, Long> {
	Optional<FlightEntity> findByFlightNumber(String flightNumber);

	Optional<List<FlightEntity>> findByDepartureAirportAndArrivalAirportAndDepartureDateAndArrivalDateAndFlightClass(
			String departure, String arrival, Date departureDate, Date arrivalDate, String flightClass);
}
