package com.flight.management.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.flight.management.domain.BookingEntity;

public interface BookingRepo extends JpaRepository<BookingEntity, Long> {

}
