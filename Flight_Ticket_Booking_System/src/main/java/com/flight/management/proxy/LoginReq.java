package com.flight.management.proxy;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginReq {
	@NotBlank(message = "Username cannot be blank.")
	private String username;

	@NotBlank(message = "Password cannot be blank.")
	private String password;
}
