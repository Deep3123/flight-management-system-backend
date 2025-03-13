package com.flight.management.domain;

import java.util.Date;

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

	private Date departureDate;

	private Date arrivalDate;

	private String departureAirport;

	private String arrivalAirport;

	private Double price;

	private Integer seatsAvailable;

	private Integer durationMinutes;

	private String airlineName;

	private Date createdAt;

	private Date updatedAt;
}
