package com.phonepe.repository;

import com.phonepe.model.Accept;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<Accept, String> {
    Optional<Accept> findByUsername(String username);
}
