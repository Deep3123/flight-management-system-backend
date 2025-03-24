package com.flight.management.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.flight.management.domain.ContactEntity;

public interface ContactRepo extends JpaRepository<ContactEntity, Long> {
	Optional<List<ContactEntity>> findByName(String name);
}
