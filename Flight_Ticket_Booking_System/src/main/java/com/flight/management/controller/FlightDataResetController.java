package com.flight.management.controller;

import com.flight.management.proxy.Response;
import com.flight.management.service.FlightDataResetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
public class FlightDataResetController {

    @Autowired
    private FlightDataResetService flightDataResetService;

    // A simple static secret for the cron job to use. 
    // In a real production app, this should be in application.properties or Vault.
    private static final String CRON_SECRET = "super-secret-cron-key-123";

    @PostMapping("/reset-flights")
    public ResponseEntity<Response> triggerFlightReset(@RequestHeader(value = "x-api-key", required = false) String apiKey) {
        
        if (apiKey == null || !apiKey.equals(CRON_SECRET)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Response.builder()
                            .message("Unauthorized: Invalid API Key")
                            .status_code(HttpStatus.UNAUTHORIZED.toString())
                            .build());
        }

        try {
            flightDataResetService.resetFlightData();
            return ResponseEntity.ok(Response.builder()
                    .message("Flight data reset successfully!")
                    .status_code(HttpStatus.OK.toString())
                    .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Response.builder()
                            .message("Failed to reset flight data: " + e.getMessage())
                            .status_code(HttpStatus.INTERNAL_SERVER_ERROR.toString())
                            .build());
        }
    }
}
