package com.flight.management.proxy;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContactUs {
	@NotBlank(message = "Name cannot be null.")
	private String name;

	@NotBlank(message = "Message cannot be null.")
	private String message;
}
