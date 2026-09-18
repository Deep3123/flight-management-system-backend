package com.flight.management.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.BulkOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.flight.management.domain.FlightEntity;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Service
@Slf4j
public class FlightDataResetService {

    @Autowired
    private MongoTemplate mongoTemplate;

    private static final List<String> INDIAN_CITIES = Arrays.asList(
            "Mumbai", "Delhi", "Bengaluru", "Hyderabad", "Ahmedabad", 
            "Chennai", "Kolkata", "Surat", "Pune", "Jaipur", 
            "Lucknow", "Kanpur", "Nagpur", "Indore", "Bhopal", 
            "Patna", "Vadodara", "Goa", "Kochi", "Amritsar"
    );

    private static final int MAX_COMBINATION_COUNT = 5;

    /**
     * This scheduled task runs every day at 11:55 PM IST.
     * It assigns random arrival and departure cities, sets dates to tomorrow,
     * and resets the available seats.
     */
    @Scheduled(cron = "0 55 23 * * ?", zone = "Asia/Kolkata")
    public void resetFlightData() {
        log.info("Starting flight data reset scheduled task with randomized cities...");

        try {
            // Calculate tomorrow's date
            LocalDate tomorrow = LocalDate.now(ZoneId.of("Asia/Kolkata")).plusDays(1);
            Date tomorrowDate = Date.from(tomorrow.atStartOfDay(ZoneId.systemDefault()).toInstant());

            // Fetch all flights to assign them unique random values
            List<FlightEntity> flights = mongoTemplate.findAll(FlightEntity.class);
            
            if (flights.isEmpty()) {
                log.info("No flights found to reset.");
                return;
            }

            // Prepare a bulk operation for efficient batch updating
            BulkOperations bulkOps = mongoTemplate.bulkOps(BulkOperations.BulkMode.UNORDERED, FlightEntity.class);
            Random random = new Random();
            
            // Keep track of how many times a specific route (e.g., "Jaipur-Mumbai") has been assigned
            Map<String, Integer> combinationCounts = new HashMap<>();

            for (FlightEntity flight : flights) {
                String depCity;
                String arrCity;
                String routeKey;

                // Pick a valid route that hasn't exceeded the max combination count
                do {
                    depCity = INDIAN_CITIES.get(random.nextInt(INDIAN_CITIES.size()));
                    arrCity = INDIAN_CITIES.get(random.nextInt(INDIAN_CITIES.size()));
                    routeKey = depCity + "-" + arrCity;
                } while (depCity.equals(arrCity) || combinationCounts.getOrDefault(routeKey, 0) >= MAX_COMBINATION_COUNT);

                // Increment the counter for this route
                combinationCounts.put(routeKey, combinationCounts.getOrDefault(routeKey, 0) + 1);

                // Create query to match this specific flight
                Query query = new Query(Criteria.where("_id").is(flight.getId()));

                // Define the unique update for this flight
                Update update = new Update()
                        .set("departureAirport", depCity)
                        .set("arrivalAirport", arrCity)
                        .set("departureDate", tomorrowDate)
                        .set("arrivalDate", tomorrowDate)
                        .set("seatsAvailable", 250)
                        .set("updatedAt", new Date());

                bulkOps.updateOne(query, update);
            }

            // Execute all updates in a single bulk operation
            var updateResult = bulkOps.execute();

            log.info("Successfully reset data and randomized cities for {} flights.", updateResult.getModifiedCount());

        } catch (Exception e) {
            log.error("Error occurred while resetting flight data: {}", e.getMessage(), e);
        }
    }
}
