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
    private String userId;
    private String accountNo;
    private String name;
    private String ifscCode;
    private String bank;
    private String branch;
    private String mobileNo;
}
