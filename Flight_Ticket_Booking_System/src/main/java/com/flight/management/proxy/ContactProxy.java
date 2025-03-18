package com.flight.management.proxy;

import java.util.Date;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContactProxy {
	@NotNull(message = "Id cannot be null.")
	private Long id;

	@NotBlank(message = "Name cannot be empty.")
	private String name;

	@NotBlank(message = "Email cannot be empty.")
	@Email(message = "Please provide a valid email address.")
	private String email;

	@NotBlank(message = "Message cannot be empty.")
	private String message;

	@Pattern(regexp = "^[0-9]{10}$", message = "Phone number must be a 10-digit number.")
	private String phoneNumber;

	private Date submittedAt; // This could be handled programmatically in the backend
}
