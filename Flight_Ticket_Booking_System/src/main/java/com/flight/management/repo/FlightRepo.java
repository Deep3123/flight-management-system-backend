package com.flight.management.repo;

<<<<<<< HEAD
import java.util.List;
=======
>>>>>>> c8b7e423bf58468af87d988072869ce6d2389aff
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.flight.management.domain.FlightEntity;

public interface FlightRepo extends JpaRepository<FlightEntity, Long> {
	Optional<FlightEntity> findByFlightNumber(String flightNumber);
<<<<<<< HEAD

	Optional<List<FlightEntity>> findByDepartureAirportAndArrivalAirport(String departure, String arrival);
=======
>>>>>>> c8b7e423bf58468af87d988072869ce6d2389aff
}
