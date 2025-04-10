package com.flight.management.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.flight.management.proxy.BookingProxy;
import com.flight.management.proxy.Response;
import com.flight.management.proxy.TicketProxy;
import com.flight.management.service.BookingService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/bookings")
public class BookingController {
	@Autowired
	private BookingService bookingService;

	@PostMapping("/verify-payment")
	public ResponseEntity<?> verifyPayment(@RequestBody BookingProxy request) {
		try {
			boolean isVerified = bookingService.verifyPayment(request.getPaymentId());

			if (isVerified) {
				return new ResponseEntity<>(new Response("success", "200"), HttpStatus.OK);
			} else {
				return new ResponseEntity<>(new Response("failure", "400"), HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			return new ResponseEntity<>(new Response("An error occurred during payment verification.",
					HttpStatus.INTERNAL_SERVER_ERROR.toString()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/confirm")
	public ResponseEntity<?> confirmBooking(@Valid @RequestBody BookingProxy bookingProxy) {
		try {
			System.err.println("Request reached here.");
			System.err.println(bookingProxy);
			bookingService.processBooking(bookingProxy);

			// Updated success response
			return new ResponseEntity<>(
					new Response("Your booking has been successfully processed.", HttpStatus.OK.toString()),
					HttpStatus.OK);
		} catch (Exception e) {
			System.err.println(e);
			// Updated error response
			return new ResponseEntity<>(new Response(
					"An unexpected error occurred while processing your booking. Please try again later or contact support if the issue persists.",
					HttpStatus.INTERNAL_SERVER_ERROR.toString()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/generate-ticket")
	public ResponseEntity<?> generateTicket(@Valid @RequestBody TicketProxy ticketProxy) {
		try {
			System.err.println("Request reached here.");
			System.err.println(ticketProxy);
			bookingService.generateTicket(ticketProxy);

			// Updated success response
			return new ResponseEntity<>(
					new Response("You will receive your booking confirmation and tickets via email shortly.",
							HttpStatus.OK.toString()),
					HttpStatus.OK);
		} catch (Exception e) {
			// Updated error response
			return new ResponseEntity<>(new Response(
					"An unexpected error occurred while processing your booking. Please try again later or contact support if the issue persists.",
					HttpStatus.INTERNAL_SERVER_ERROR.toString()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
