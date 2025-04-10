package com.flight.management.proxy;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PassengerProxy {
	private Long id;

	@NotBlank(message = "First name is required.")
	private String firstName;

	@NotBlank(message = "Last name is required.")
	private String lastName;

	@NotNull(message = "Age is required.")
	@Min(value = 1, message = "Age must be at least 1.")
	@Max(value = 120, message = "Age must not exceed 120.")
	private int age;

	@NotBlank(message = "Email is required.")
	@Email(message = "Invalid email format.")
	private String email;

	@NotBlank(message = "Country code is required.")
	@Pattern(regexp = "^\\+?[0-9]{1,5}$", message = "Invalid country code format.")
	private String countryCode;

	@NotBlank(message = "Mobile number is required.")
	@Pattern(regexp = "^[0-9]{7,15}$", message = "Mobile number must contain 7 to 15 digits.")
	private String mobile;
}
