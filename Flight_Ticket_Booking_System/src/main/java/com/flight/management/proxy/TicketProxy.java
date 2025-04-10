package com.flight.management.proxy;

import java.util.List;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TicketProxy {
	@NotBlank(message = "Payment ID cannot be blank.")
	private String paymentId;

	@NotNull(message = "Flight details cannot be null.")
	private FlightProxy flight;

	@NotNull(message = "Passenger details cannot be null.")
	private List<PassengerProxy> passengers;

	@NotNull(message = "Total amount cannot be null.")
	@Positive(message = "Amount must be positive.")
	private Double amount;
}
