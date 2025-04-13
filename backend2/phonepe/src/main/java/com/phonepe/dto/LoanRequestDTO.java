package com.phonepe.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoanRequestDTO {
    private String borrowerName;
    private String borrowerLocation;
    private double amount;
    private int duration;
    private double creditScore;
    private double rating;

    // Lombok will automatically generate getters, setters, toString, equals, and hashCode
    // No need to manually define them

    // If you need a no-args constructor (required for some frameworks like JPA), 
    // you can use the @NoArgsConstructor annotation:
    // @NoArgsConstructor
}
