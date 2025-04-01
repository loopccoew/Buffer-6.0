package com.phonepe.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.phonepe.model.Accept;
import com.phonepe.repository.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void registerUser(Accept user) {
        userRepository.save(user);
    }

    public Accept getUser(String userId) {
        return userRepository.findById(userId).orElse(null);
    }

    public List<Accept> getAllUsers() {
        return userRepository.findAll();
    }
}
