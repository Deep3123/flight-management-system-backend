package com.flight.management.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.flight.management.domain.BookingEntity;

public interface BookingRepo extends JpaRepository<BookingEntity, Long> {
	Optional<BookingEntity> findByPaymentId(String paymentId);
}
