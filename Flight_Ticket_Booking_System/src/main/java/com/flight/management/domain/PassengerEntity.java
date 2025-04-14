//package com.flight.management.domain;
//
//import jakarta.persistence.Entity;
//import jakarta.persistence.GeneratedValue;
//import jakarta.persistence.GenerationType;
//import jakarta.persistence.Id;
//import jakarta.persistence.Table;
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//@Data
//@Entity
//@NoArgsConstructor
//@AllArgsConstructor
//@Table(name = "passenger")
//public class PassengerEntity {
//
//	@Id
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
//	private Long id;
//
//	private String firstName;
//
//	private String lastName;
//
//	private int age;
//
//	private String email;
//
//	private String countryCode;
//	
//	private String mobile;
//}

package com.flight.management.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "passenger") // Replaces @Entity and @Table for MongoDB
public class PassengerEntity {

	@Id // Replaces @Id from JPA
	private String id; // MongoDB typically uses String for IDs, so it will be ObjectId in MongoDB by
						// default

	private String firstName;

	private String lastName;

	private int age;

	private String email;

	private String countryCode;

	private String mobile;
}
