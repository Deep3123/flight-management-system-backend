package com.flight.management.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.flight.management.proxy.BookingProxy;
import com.flight.management.service.BookingService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/bookings")
public class BookingController {
	@Autowired
	private BookingService bookingService;

	@PostMapping("/confirm")
	public ResponseEntity<?> confirmBooking(@Valid @RequestBody BookingProxy bookingProxy) {
		try {
			System.err.println("Request reached here.");
			bookingService.processBooking(bookingProxy);
			return ResponseEntity.ok("Booking successful");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Booking failed");
		}
	}

}
