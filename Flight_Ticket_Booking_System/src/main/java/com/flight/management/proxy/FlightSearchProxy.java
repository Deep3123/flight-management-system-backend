package com.flight.management.proxy;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FlightSearchProxy {

	private String departureAirport;

	private String arrivalAirport;

	private Date departureDate;

	private Date arrivalDate;

	private Long personCount;

	private String flightClass;
}
