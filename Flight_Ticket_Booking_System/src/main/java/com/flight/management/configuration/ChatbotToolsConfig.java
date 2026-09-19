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
    @Description("Search for flights by origin and/or destination cities. Omit parameters if the user didn't specify them.")
    public Function<FlightSearchRequest, List<FlightEntity>> flightSearchTool(FlightRepo flightRepo) {
        return request -> {
            List<FlightEntity> allFlights = flightRepo.findAll();
            return allFlights.stream()
                    .filter(f -> {
                        boolean matchDep = request.departure() == null || request.departure().trim().isEmpty() ||
                                (f.getDepartureAirport() != null && f.getDepartureAirport().toLowerCase().contains(request.departure().trim().toLowerCase()));
                        boolean matchArr = request.arrival() == null || request.arrival().trim().isEmpty() ||
                                (f.getArrivalAirport() != null && f.getArrivalAirport().toLowerCase().contains(request.arrival().trim().toLowerCase()));
                        return matchDep && matchArr;
                    })
                    .collect(Collectors.toList());
        };
    }

}
