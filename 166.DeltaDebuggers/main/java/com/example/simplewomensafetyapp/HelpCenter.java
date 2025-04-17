package com.example.simplewomensafetyapp;

import java.io.Serializable;

public class HelpCenter implements Serializable {
    private String name;
    private String address;
    private String phone;
    private double latitude;
    private double longitude;

    public HelpCenter(String name, String address, String phone, double latitude, double longitude) {
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    // Getters and setters for each field
    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getPhone() {
        return phone;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}
