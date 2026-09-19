package com.flight.management.configuration;

import com.flight.management.domain.BookingEntity;
import com.flight.management.domain.FlightEntity;
import com.flight.management.repo.BookingRepo;
import com.flight.management.repo.FlightRepo;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Configuration
public class ChatbotToolsConfig {

    public record FlightSearchRequest(String departure, String arrival) {}

    @Bean
    @Description("Search for flights by origin and destination cities")
    public Function<FlightSearchRequest, List<FlightEntity>> flightSearchTool(FlightRepo flightRepo) {
        return request -> {
            List<FlightEntity> allFlights = flightRepo.findAll();
            return allFlights.stream()
                    .filter(f -> f.getDepartureAirport().equalsIgnoreCase(request.departure()) &&
                                 f.getArrivalAirport().equalsIgnoreCase(request.arrival()))
                    .collect(Collectors.toList());
        };
    }

    public record BookingLookupRequest(String pnr) {}

    @Bean
    @Description("Lookup booking details for the currently logged-in user. Use this when the user asks about their bookings. It automatically enforces security so the AI does not need to ask for their email.")
    public Function<BookingLookupRequest, List<BookingEntity>> bookingLookupTool(BookingRepo bookingRepo) {
        return request -> {
            String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
            
            if (currentUserEmail == null || currentUserEmail.equals("anonymousUser")) {
                // If not authenticated, return empty list
                return List.of(); 
            }

            // Fetch all and filter by current user's email only (Security Enforcement)
            List<BookingEntity> allBookings = bookingRepo.findAll();
            return allBookings.stream()
                    .filter(b -> b.getPassenger() != null && currentUserEmail.equalsIgnoreCase(b.getPassenger().getEmail()))
                    .filter(b -> request.pnr() == null || request.pnr().isEmpty() || 
                                 request.pnr().equals(b.getId()) || 
                                 request.pnr().equals(b.getPaymentId()))
                    .collect(Collectors.toList());
        };
    }
}
