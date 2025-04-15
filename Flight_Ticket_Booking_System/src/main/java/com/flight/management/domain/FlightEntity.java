//package com.flight.management.domain;
//
//import java.util.Date;
//import org.hibernate.annotations.CreationTimestamp;
//import org.hibernate.annotations.UpdateTimestamp;
//import jakarta.persistence.Entity;
//import jakarta.persistence.GeneratedValue;
//import jakarta.persistence.GenerationType;
//import jakarta.persistence.Id;
//import jakarta.persistence.Table;
//import jakarta.persistence.Temporal;
//import jakarta.persistence.TemporalType;
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//@Entity
//@Table(name = "flight")
//public class FlightEntity {
//	@Id
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
//	private Long id;
//
//	private String flightNumber;
//
//	@Temporal(TemporalType.DATE)
//	private Date departureDate;
//
//	private String departureTime;
//
//	@Temporal(TemporalType.DATE)
//	private Date arrivalDate;
//
//	private String arrivalTime;
//
//	private String departureAirport;
//
//	private String arrivalAirport;
//
//	private Double price;
//
//	private Integer seatsAvailable;
//
//	private Integer durationMinutes;
//
////	private String airlineName;
//
//	private String flightClass;
//
//	@CreationTimestamp
//	private Date createdAt;
//
//	@UpdateTimestamp
//	private Date updatedAt;
//}

package com.flight.management.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Document(collection = "flight") // Replaces @Entity and @Table for MongoDB
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FlightEntity {

	@Id // Replaces @Id from JPA
	private String id; // MongoDB typically uses String for IDs, so it will be ObjectId in MongoDB by
						// default

	private String flightNumber;

	private Date departureDate; // MongoDB handles Date directly, no need for @Temporal

	private String departureTime;

	private Date arrivalDate; // MongoDB handles Date directly, no need for @Temporal

	private String arrivalTime;

	private String departureAirport;

	private String arrivalAirport;

	private Double price;

	private Integer seatsAvailable;

	private Integer durationMinutes;

	private String flightClass;

	private Date createdAt; // MongoDB can store the date directly (use custom logic to populate)

	private Date updatedAt; // MongoDB can store the date directly (use custom logic to populate)

}
