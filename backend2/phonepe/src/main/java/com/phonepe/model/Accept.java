package com.phonepe.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "users") // MongoDB collection name
public class Accept {

    @Id
    private String userId; // Auto-generated unique ID
    private String name;    // User's full name
    private String mobileNo; // User's mobile number
    private String city;    // User's city
    private String area;    // User's area
    private String username; // Username for login
    private String password; // Password for login (ensure you hash this in practice)

}
