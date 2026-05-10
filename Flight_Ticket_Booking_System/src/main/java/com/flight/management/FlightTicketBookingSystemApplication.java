package com.flight.management;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import io.github.cdimascio.dotenv.Dotenv;

@SpringBootApplication
@org.springframework.scheduling.annotation.EnableScheduling
public class FlightTicketBookingSystemApplication {
	public static void main(String[] args) {
		// Dotenv dotenv = Dotenv.load();

		// System.out.println("Mongo URI: " + dotenv.get("SPRING_DATA_MONGODB_URI"));

		SpringApplication.run(FlightTicketBookingSystemApplication.class, args);
	}

//	@Bean
//	public PasswordEncoder encoder() {
//		return new BCryptPasswordEncoder();
//	}
}