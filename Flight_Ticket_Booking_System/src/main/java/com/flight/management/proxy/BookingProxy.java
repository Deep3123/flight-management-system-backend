//package com.flight.management.proxy;
//
//import java.util.Date;
//import jakarta.validation.constraints.Email;
//import jakarta.validation.constraints.NotBlank;
//import jakarta.validation.constraints.NotNull;
//import jakarta.validation.constraints.Pattern;
//import jakarta.validation.constraints.Positive;
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//public class BookingProxy {
//	private Long id;
//
//	@NotBlank(message = "Payment ID cannot be blank.")
//	private String paymentId;
//
//	@NotNull(message = "Flight details cannot be null.")
//	private String flightId;
//
//	@NotNull(message = "Passenger details cannot be null.")
//	private PassengerProxy passenger;
//
//	@NotNull(message = "Total amount cannot be null.")
//	@Positive(message = "Amount must be positive.")
//	private Double amount;
//
//	@NotNull(message = "Count cannot be null.")
//	@Positive(message = "Count must be positive.")
//	private Integer count;
//
//	private Date bookingDate; // optional, or populate in backend
//}



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
	private String id;

	@NotBlank(message = "Payment ID cannot be blank.")
	private String paymentId;

	@NotNull(message = "Flight details cannot be null.")
	private String flightId;

	@NotNull(message = "Passenger details cannot be null.")
	private PassengerProxy passenger;

	@NotNull(message = "Total amount cannot be null.")
	@Positive(message = "Amount must be positive.")
	private Double amount;

	@NotNull(message = "Count cannot be null.")
	@Positive(message = "Count must be positive.")
	private Integer count;

	private Date bookingDate; // optional, or populate in backend
}