package com.flight.management.repo;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.flight.management.domain.PassengerEntity;

public interface PassengerRepo extends MongoRepository<PassengerEntity, String> {

}
