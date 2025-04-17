package com.example.simplewomensafetyapp;

public class ContactModel {
    private String name;
    private String relation;
    private String phone;

    public ContactModel(String name, String relation, String phone) {
        this.name = name;
        this.relation = relation;
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public String getRelation() {
        return relation;
    }

    public String getPhone() {
        return phone;
    }
}

