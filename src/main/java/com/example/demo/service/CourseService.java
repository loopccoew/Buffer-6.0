package com.example.demo.service;

import com.example.demo.model.CourseInfo;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CourseService {
    
	   // Fetch courses from the CourseSearcher class
    
	List<CourseInfo> allCourses = getAllCourses();
    // Method to search and filter courses

    // Method to search and filter courses
	public List<CourseInfo> searchAndFilterCourses(String name, String minDuration, String maxDuration) {
	    List<CourseInfo> filteredCourses = new ArrayList<>();

	    // If the user provided no filters, return all courses
	    if ((minDuration == null || minDuration.isEmpty() || Integer.parseInt(minDuration) == 0) &&
	        (maxDuration == null || maxDuration.isEmpty() || Integer.parseInt(maxDuration) == 0)) {
	        
	        // If no valid duration filters, just filter by name if given
	        return searchAndFilterCourses(name);
	    }

	    // Filter based on name and duration if provided
	    for (CourseInfo course : allCourses) {
	        boolean matches = true;

	        // Filter by course name if provided
	        if (name != null && !name.isEmpty() && !course.getCourseName().toLowerCase().contains(name.toLowerCase())) {
	            matches = false;
	        }

	        // If minDuration is provided and valid, filter by duration
	        if (minDuration != null && !minDuration.isEmpty() && Integer.parseInt(minDuration) > 0) {
	            try {
	                int minDur = Integer.parseInt(minDuration);
	                if (Integer.parseInt(course.getDuration()) < minDur) {
	                    matches = false;
	                }
	            } catch (NumberFormatException e) {
	                matches = false;
	            }
	        }

	        // If maxDuration is provided and valid, filter by duration
	        if (maxDuration != null && !maxDuration.isEmpty() && Integer.parseInt(maxDuration) > 0) {
	            try {
	                int maxDur = Integer.parseInt(maxDuration);
	                if (Integer.parseInt(course.getDuration()) > maxDur) {
	                    matches = false;
	                }
	            } catch (NumberFormatException e) {
	                matches = false;
	            }
	        }

	        // If the course matches all filters, add it to the result
	        if (matches) {
	            filteredCourses.add(course);
	        }
	    }

	    return filteredCourses;
	}

	// Function to handle searching only by name (overloading)
	public List<CourseInfo> searchAndFilterCourses(String name) {
	    List<CourseInfo> filteredCourses = new ArrayList<>();
	    for (CourseInfo course : allCourses) {
	        if (name != null && !name.isEmpty() && course.getCourseName().toLowerCase().contains(name.toLowerCase())) {
	            filteredCourses.add(course);
	        }
	    }
	    return filteredCourses;
	}

    

    private List<CourseInfo> courseList = new ArrayList<>();

    // Fetch all courses
    public List<CourseInfo> getAllCourses() {
        return courseList;
    }

    // Add a new course
    public void addCourse(CourseInfo course) {
        courseList.add(course);
    }

    // Update an existing course
    public void updateCourse(CourseInfo updatedCourse) {
        for (CourseInfo course : courseList) {
            if (course.getCourseId().equals(updatedCourse.getCourseId())) {
                course.setCourseName(updatedCourse.getCourseName());
                course.setDuration(updatedCourse.getDuration());
                course.setCostStatus(updatedCourse.getCostStatus());
                course.setLevel(updatedCourse.getLevel());
                course.setRating(updatedCourse.getRating());
                course.setReview(updatedCourse.getReview());
            }
        }
    }

    // Delete a course by ID
    public void deleteCourse(String courseId) {
        courseList.removeIf(course -> course.getCourseId().equals(courseId));
    }

    // Rank courses by rating
    public List<CourseInfo> getRankedCourses() {
        return courseList.stream()
                .sorted((course1, course2) -> Double.compare(course2.getRating(), course1.getRating()))  // Sort by rating in descending order
                .collect(Collectors.toList());
    }

    // Sort courses by duration
    public List<CourseInfo> getCoursesSortedByDuration() {
        return courseList.stream()
                .sorted((course1, course2) -> Integer.compare(Integer.parseInt(course1.getDuration()), Integer.parseInt(course2.getDuration())))
                .collect(Collectors.toList());
    }
}
