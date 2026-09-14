package com.flight.management.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.flight.management.domain.FlightEntity;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

@Service
@Slf4j
public class FlightDataResetService {

    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * This scheduled task runs every day at 11:55 PM IST.
     * It uses a bulk update query to set departure and arrival dates to tomorrow,
     * and resets the available seats.
     */
    @Scheduled(cron = "0 55 23 * * ?", zone = "Asia/Kolkata")
    public void resetFlightData() {
        log.info("Starting flight data reset scheduled task via bulk update...");

        try {
            // Calculate tomorrow's date
            LocalDate tomorrow = LocalDate.now(ZoneId.of("Asia/Kolkata")).plusDays(1);
            Date tomorrowDate = Date.from(tomorrow.atStartOfDay(ZoneId.systemDefault()).toInstant());

            // Create an empty query to match all documents
            Query query = new Query();

            // Define the update operations
            Update update = new Update()
                    .set("departureDate", tomorrowDate)
                    .set("arrivalDate", tomorrowDate)
                    .set("seatsAvailable", 250)
                    .set("updatedAt", new Date());

            // Execute the bulk update
            var updateResult = mongoTemplate.updateMulti(query, update, FlightEntity.class);

            log.info("Successfully reset data for {} flights.", updateResult.getModifiedCount());

        } catch (Exception e) {
            log.error("Error occurred while resetting flight data: {}", e.getMessage(), e);
        }
    }
}
