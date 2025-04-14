//package com.flight.management.repo;
//
//import java.util.Optional;
//
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.stereotype.Repository;
//
//import com.flight.management.domain.UserEntity;
//
//import jakarta.transaction.Transactional;
//
//@Repository
//@Transactional
//public interface UserRepo extends JpaRepository<UserEntity, Long> {
//
//	Optional<UserEntity> findByUsername(String username);
//
//	Optional<UserEntity> findByEmailId(String email);
//
//}

package com.flight.management.repo;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.flight.management.domain.UserEntity;

@Repository
public interface UserRepo extends MongoRepository<UserEntity, String> { // Use String or ObjectId as ID for MongoDB

	Optional<UserEntity> findByUsername(String username);

	Optional<UserEntity> findByEmailId(String email);
}
