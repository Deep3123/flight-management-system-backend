package com.flight.management.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.flight.management.proxy.BookingDetails;
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
//			System.err.println("Request reached here.");
//			System.err.println(bookingProxy);
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
//			System.err.println("Request reached here.");
//			System.err.println(ticketProxy);
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

	@GetMapping("/all-bookings")
	public ResponseEntity<?> getAllBookings() {
		List<BookingDetails> list = bookingService.getAllMergedBookings();

		if (list != null && !list.isEmpty())
			return new ResponseEntity<>(list, HttpStatus.OK);

		else
			return new ResponseEntity<>(
					new Response("No data found to display currently!!", HttpStatus.NOT_FOUND.toString()),
					HttpStatus.NOT_FOUND);
	}

	@PostMapping("/delete-booking-details")
	public ResponseEntity<?> deleteBookingDetails(@Valid @RequestBody String paymentId) {
		String s = bookingService.deleteBookingDetails(paymentId);

		if (s != null && !s.isEmpty())
			return new ResponseEntity<>(new Response(s, HttpStatus.OK.toString()), HttpStatus.OK);

		else
			return new ResponseEntity<>(new Response(
					"Either booking details not found or there is an error generated during deletion of booking details.",
					HttpStatus.BAD_REQUEST.toString()), HttpStatus.BAD_REQUEST);
	}

    @GetMapping("/download-all-booking-data")
    public ResponseEntity<?> downloadAllBookingData() {
        Response resp = bookingService.downloadAllBookingData();

        if (resp.getStatus_code().equals(HttpStatus.OK.toString())) {
            return new ResponseEntity<>(resp, HttpStatus.OK);
        }

        return new ResponseEntity<>(resp, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
