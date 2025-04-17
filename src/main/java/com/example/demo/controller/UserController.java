package com.example.demo.controller;

import com.example.demo.model.User;  // Import the User model
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class UserController {

    @PostMapping("/login")
    public String login(@RequestBody User user) {
        // Hardcoded credentials for student and admin (to be replaced with a database later)
        if (user.getId().equals("student") && user.getPassword().equals("student123")) {
            return "student";  // For student
        } else if (user.getId().equals("admin") && user.getPassword().equals("admin123")) {
            return "admin";  // For admin
        } else {
            return "invalid";  // Invalid login credentials
        }
    }
}
