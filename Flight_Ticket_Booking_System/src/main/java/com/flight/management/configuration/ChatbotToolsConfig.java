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

}
