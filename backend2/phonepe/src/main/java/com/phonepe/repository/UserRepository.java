package com.phonepe.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.phonepe.model.Accept;

@Repository
public interface UserRepository extends MongoRepository<Accept, String> {
    
}
