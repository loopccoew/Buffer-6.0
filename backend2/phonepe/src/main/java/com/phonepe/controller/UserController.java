package com.phonepe.controller;

import com.phonepe.model.Accept;
import com.phonepe.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public String registerUser(@RequestBody Accept user) {
        userService.registerUser(user);
        return "User registered successfully!";
    }

    @GetMapping("/{userId}")
    public Accept getUser(@PathVariable String userId) {
        return userService.getUser(userId);
    }

    @GetMapping("/all")
    public List<Accept> getAllUsers() {
        return userService.getAllUsers();
    }
}
