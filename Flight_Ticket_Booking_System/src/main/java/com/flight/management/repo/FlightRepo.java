//package com.flight.management.repo;
//
//import java.util.Date;
//import java.util.List;
//import java.util.Optional;
//import org.springframework.data.jpa.repository.JpaRepository;
//import com.flight.management.domain.FlightEntity;
//
//public interface FlightRepo extends JpaRepository<FlightEntity, Long> {
//	Optional<FlightEntity> findByFlightNumber(String flightNumber);
//
//	Optional<List<FlightEntity>> findByDepartureAirportAndArrivalAirportAndDepartureDateAndArrivalDateAndFlightClass(
//			String departure, String arrival, Date departureDate, Date arrivalDate, String flightClass);
//}

package com.flight.management.repo;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.flight.management.domain.FlightEntity;

public interface FlightRepo extends MongoRepository<FlightEntity, String> { // Use String or ObjectId as ID for MongoDB

	Optional<FlightEntity> findByFlightNumber(String flightNumber);

//	Optional<List<FlightEntity>> findByDepartureAirportAndArrivalAirportAndDepartureDateAndArrivalDateAndFlightClass(
//			String departure, String arrival, Date departureDate, Date arrivalDate, String flightClass);

//	List<FlightEntity> findByDepartureAirportAndArrivalAirportAndDepartureDateAndArrivalDateAndFlightClass(
//			String departure, String arrival, Date departureDate, Date arrivalDate, String flightClass);

	@Query("{ 'departureAirport': ?0, 'arrivalAirport': ?1, 'departureDate': { $gte: ?2, $lte: ?3 }, 'arrivalDate': { $gte: ?4, $lte: ?5 }, 'flightClass': ?6 }")
	List<FlightEntity> findFlightsInRange(String depAirport, String arrAirport, Date depStart, Date depEnd,
			Date arrStart, Date arrEnd, String flightClass);

}
