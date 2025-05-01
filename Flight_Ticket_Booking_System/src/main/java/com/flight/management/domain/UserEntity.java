//package com.flight.management.domain;
//
//import java.util.Date;
//
//import org.hibernate.annotations.CreationTimestamp;
//import org.hibernate.annotations.UpdateTimestamp;
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
//@NoArgsConstructor
//@AllArgsConstructor
//@Entity
//@Table(name = "user")
//public class UserEntity {
//	@Id
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
//	private Long id;
//
//	private String name;
//
//	private String emailId;
//
//	private Long mobileNo;
//
//	private String username;
//
//	private String password;
//
//	private String role = "USER";
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

@Document(collection = "user") // Replaces @Entity and @Table for MongoDB
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {

	@Id // Replaces @Id from JPA
	private String id; // MongoDB typically uses String for IDs, so it will be ObjectId in MongoDB by
						// default

	private String name;	

	private String emailId;

	private Long mobileNo;

	private String username;

	private String password;

	private String role = "USER"; // Default value for role

	private Date createdAt; // MongoDB can handle Date objects directly (no need for @CreationTimestamp)

	private Date updatedAt; // MongoDB can handle Date objects directly (no need for @UpdateTimestamp)
}
