//package com.flight.management.repo;
//
//import java.util.List;
//import java.util.Optional;
//
//import org.springframework.data.jpa.repository.JpaRepository;
//
//import com.flight.management.domain.ContactEntity;
//
//public interface ContactRepo extends JpaRepository<ContactEntity, Long> {
//	Optional<List<ContactEntity>> findByName(String name);
//
//	Optional<ContactEntity> findByNameAndMessage(String name, String message);
//}


package com.flight.management.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.flight.management.domain.ContactEntity;

public interface ContactRepo extends MongoRepository<ContactEntity, String> { // Use String as the ID type for MongoDB

	Optional<List<ContactEntity>> findByName(String name);

	Optional<ContactEntity> findByNameAndMessage(String name, String message);
}
