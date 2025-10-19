package com.flight.management.proxy;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Response {
	@NotBlank(message = "Response message should not be null.")
	private String message;

	@NotBlank(message = "Status code should not be null.")
	private String status_code;

    private Object data;

    public Response(String message, String status_code) {
        this.message = message;
        this.status_code = status_code;
    }
}
