package com.flight.management.proxy;

import java.util.Date;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingProxy {
	private Long id;

	@NotBlank(message = "Payment ID cannot be blank.")
	private String paymentId;

	@NotNull(message = "Flight details cannot be null.")
	private FlightProxy flightId;

	@NotNull(message = "Passenger details cannot be null.")
	private PassengerProxy passenger;

	@NotNull(message = "Total amount cannot be null.")
	@Positive(message = "Amount must be positive.")
	private Double amount;

	private Date bookingDate; // optional, or populate in backend

	// Optional for convenience, can extract from PassengerProxy in service layer
	public String getPrimaryPassengerName() {
		return passenger.getFirstName() + " " + passenger.getLastName();
	}

	public String getEmail() {
		return passenger.getEmail();
	}

	public String getPhone() {
		return passenger.getMobile();
	}

}