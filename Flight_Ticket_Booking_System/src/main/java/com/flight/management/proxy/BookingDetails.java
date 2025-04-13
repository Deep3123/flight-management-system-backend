package com.flight.management.proxy;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingDetails {
	private Long id;
	private String paymentId;
	private String flightId;
	private Double amount;
	private Integer count;
	private Date bookingDate;

	// Passenger fields
	private String firstName;
	private String lastName;
	private int age;
	private String email;
	private String countryCode;
	private String mobile;
}
