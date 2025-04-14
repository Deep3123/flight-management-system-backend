//package com.flight.management.repo;
//
//import java.util.Optional;
//
//import org.springframework.data.jpa.repository.JpaRepository;
//
//import com.flight.management.domain.BookingEntity;
//
//public interface BookingRepo extends JpaRepository<BookingEntity, Long> {
//	Optional<BookingEntity> findByPaymentId(String paymentId);
//}

package com.flight.management.repo;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.flight.management.domain.BookingEntity;

public interface BookingRepo extends MongoRepository<BookingEntity, String> { // Use String instead of Long for MongoDB
																				// IDs

	Optional<BookingEntity> findByPaymentId(String paymentId);

}
