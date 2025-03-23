package com.flight.management.proxy;

import java.util.Date;

import org.hibernate.validator.constraints.Range;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FlightProxy {
	private Long id;

	@NotBlank(message = "Flight number cannot be null or empty.")
	@Pattern(regexp = "^[A-Z0-9]+$", message = "Flight number must contain only uppercase letters and numbers.")
	private String flightNumber;

	@NotNull(message = "Departure date cannot be null.")
	@FutureOrPresent(message = "Departure date must be today or in the future.")
	private Date departureDate;

	@NotNull(message = "Arrival date cannot be null.")
	@FutureOrPresent(message = "Arrival date must be today or in the future.")
	private Date arrivalDate;

	@NotBlank(message = "Departure airport cannot be null or empty.")
	@Pattern(regexp = "^[A-Za-z0-9\\s-]+$", message = "Departure airport can only contain letters, numbers, spaces, and hyphens.")
	private String departureAirport;

	@NotBlank(message = "Arrival airport cannot be null or empty.")
	@Pattern(regexp = "^[A-Za-z0-9\\s-]+$", message = "Arrival airport can only contain letters, numbers, spaces, and hyphens.")
	private String arrivalAirport;

	@NotNull(message = "Price cannot be null.")
	@DecimalMin(value = "0.01", message = "Price must be greater than 0.")
	@DecimalMax(value = "10000.00", message = "Price must not exceed 10000.")
	private Double price;

	@NotNull(message = "Seats available cannot be null.")
	@Range(min = 1, max = 500, message = "Seats available must be between 1 and 500.")
	private Integer seatsAvailable;

	@NotNull(message = "Duration cannot be null.")
	@Range(min = 30, max = 1440, message = "Duration must be between 30 minutes and 24 hours (1440 minutes).")
	private Integer durationMinutes;

	@NotBlank(message = "Airline name cannot be null or empty.")
	@Pattern(regexp = "^[A-Za-z\\s-]+$", message = "Airline name can only contain letters, spaces, and hyphens.")
	private String airlineName;

	@NotBlank(message = "Flight class cannot be null or empty.")
	@Pattern(regexp = "^[A-Za-z\\s-]+$", message = "Flight class can only contain letters, spaces, and hyphens.")
	private String flightClass;

	@PastOrPresent(message = "Created date cannot be in the future.")
	private Date createdAt;

	@PastOrPresent(message = "Updated date cannot be in the future.")
	private Date updatedAt;
}
