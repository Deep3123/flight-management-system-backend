//package com.flight.management.domain;
//
//import java.util.Date;
//
//import org.hibernate.annotations.CreationTimestamp;
//
//import com.flight.management.proxy.FlightProxy;
//
//import jakarta.persistence.CascadeType;
//import jakarta.persistence.Entity;
//import jakarta.persistence.GeneratedValue;
//import jakarta.persistence.GenerationType;
//import jakarta.persistence.Id;
//import jakarta.persistence.OneToOne;
//import jakarta.persistence.Table;
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//@Entity
//@Table(name = "bookings")
//public class BookingEntity {
//
//	@Id
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
//	private Long id;
//
//	private String paymentId;
//
//	private String flightId;
//
//	@OneToOne(cascade = CascadeType.ALL)
//	private PassengerEntity passenger;
//
//	private Double amount;
//
//	private Integer count;
//
//	@CreationTimestamp
//	private Date bookingDate;
//}

package com.flight.management.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Document(collection = "bookings") // Replaces @Entity and @Table for MongoDB
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingEntity {

	@Id // Replaces @Id from JPA
	private String id; // MongoDB typically uses String for IDs

	private String paymentId;

	private String flightId;

	@DBRef // Replaces @OneToOne for referencing another document
	private PassengerEntity passenger; // PassengerEntity will be referenced by ObjectId in MongoDB

	private Double amount;

	private Integer count;

	private Date bookingDate; // MongoDB handles Date directly, no need for @CreationTimestamp
}
