package com.flight.management.domain;

import java.util.Date;

<<<<<<< HEAD
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

=======
>>>>>>> c8b7e423bf58468af87d988072869ce6d2389aff
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "flight")
public class FlightEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String flightNumber;

<<<<<<< HEAD
	private Date departureDateAndTime;

	private Date arrivalDateAndTime;
=======
	private Date departureDate;

	private Date arrivalDate;
>>>>>>> c8b7e423bf58468af87d988072869ce6d2389aff

	private String departureAirport;

	private String arrivalAirport;

	private Double price;

	private Integer seatsAvailable;

	private Integer durationMinutes;

	private String airlineName;

<<<<<<< HEAD
	@CreationTimestamp
	private Date createdAt;

	@UpdateTimestamp
=======
	private Date createdAt;

>>>>>>> c8b7e423bf58468af87d988072869ce6d2389aff
	private Date updatedAt;
}
