package com.flight.management.domain;

import java.util.Date;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
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

	@Temporal(TemporalType.DATE)
	private Date departureDate;

	private String departureTime;

	@Temporal(TemporalType.DATE)
	private Date arrivalDate;

	private String arrivalTime;

	private String departureAirport;

	private String arrivalAirport;

	private Double price;

	private Integer seatsAvailable;

	private Integer durationMinutes;

//	private String airlineName;

	private String flightClass;

	@CreationTimestamp
	private Date createdAt;

	@UpdateTimestamp
	private Date updatedAt;
}
