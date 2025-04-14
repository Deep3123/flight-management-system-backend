//package com.flight.management.domain;
//
//import java.util.Date;
//
//import org.hibernate.annotations.CreationTimestamp;
//
//import jakarta.persistence.Entity;
//import jakarta.persistence.GeneratedValue;
//import jakarta.persistence.GenerationType;
//import jakarta.persistence.Id;
//import jakarta.persistence.Lob;
//import jakarta.persistence.Table;
//import jakarta.persistence.Temporal;
//import jakarta.persistence.TemporalType;
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//@Entity
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//@Table(name = "contact_us")
//public class ContactEntity {
//
//	@Id
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
//	private Long id;
//
//	private String name;
//
//	private String email;
//
//	private String phoneNumber;
//
//	@Lob
//	private String message;
//
//	@Temporal(TemporalType.TIMESTAMP)
//	@CreationTimestamp
//	private Date submittedAt;
//
//}

package com.flight.management.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Document(collection = "contactUs") // Replaces @Entity and @Table for MongoDB
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContactEntity {

	@Id // Replaces @Id from JPA
	private String id; // MongoDB typically uses String for IDs instead of Long

	private String name;

	private String email;

	private String phoneNumber;

	@Field("message") // Optional: You can use @Field to map the MongoDB field if needed
	private String message;

	private Date submittedAt; // MongoDB stores the Date object, no need for @Temporal or @CreationTimestamp
}
