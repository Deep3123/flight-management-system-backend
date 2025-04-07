package com.flight.management.proxy;

import java.time.LocalDateTime;
import java.util.Date;

import org.hibernate.validator.constraints.Range;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FlightSearchProxy {
	@NotBlank(message = "Departure airport cannot be null or empty.")
	@Pattern(regexp = "^[A-Za-z0-9\\s-]+$", message = "Departure airport can only contain letters, numbers, spaces, and hyphens.")
	private String departureAirport;

	@NotBlank(message = "Arrival airport cannot be null or empty.")
	@Pattern(regexp = "^[A-Za-z0-9\\s-]+$", message = "Arrival airport can only contain letters, numbers, spaces, and hyphens.")
	private String arrivalAirport;

//	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@FutureOrPresent(message = "Departure date must be today or in the future.")
	@NotNull(message = "Departure date cannot be null.")
	private Date departureDate;

//	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@FutureOrPresent(message = "Arrival date must be today or in the future.")
	@NotNull(message = "Arrival date cannot be null.")
	private Date arrivalDate;

	@NotNull(message = "Person count cannot be null.")
	@Range(min = 1, max = 9, message = "Person count must be between 1 and 9.")
	private Long personCount;

	@NotBlank(message = "Flight class cannot be null or empty.")
	@Pattern(regexp = "^[A-Za-z\\s-]+$", message = "Flight class can only contain letters, spaces, and hyphens.")
	private String flightClass;
}
