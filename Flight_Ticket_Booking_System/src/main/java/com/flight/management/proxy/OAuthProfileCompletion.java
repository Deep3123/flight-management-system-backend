package com.flight.management.proxy;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OAuthProfileCompletion {

	@NotBlank(message = "Email is required")
	private String email;

	@NotBlank(message = "Name is required")
	private String name;

	@NotBlank(message = "Username is required")
	private String username;

	@NotNull(message = "Mobile number is required")
	private Long mobileNo;
}
