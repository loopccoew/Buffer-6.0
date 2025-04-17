package com.example.demo.model;
public class CourseInfo {
    private String courseId;
    private String courseName;
    private String duration;
    private String validity;
    private String costStatus; // This might be the 'price' in your front-end
    private String level;
    private boolean certification;
    private double rating;
    private String review; // This might be the 'description' in your front-end

    // Constructor
    public CourseInfo(String courseId, String courseName, String duration, String validity, 
                      String costStatus, String level, boolean certification, double rating, String review) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.duration = duration;
        this.validity = validity;
        this.costStatus = costStatus;
        this.level = level;
        this.certification = certification;
        this.rating = rating;
        this.review = review;
    }

    // Getters and Setters
    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getValidity() {
        return validity;
    }

    public void setValidity(String validity) {
        this.validity = validity;
    }

    public String getCostStatus() {
        return costStatus;
    }

    public void setCostStatus(String costStatus) {
        this.costStatus = costStatus;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public boolean isCertification() {
        return certification;
    }

    public void setCertification(boolean certification) {
        this.certification = certification;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }
    
}
