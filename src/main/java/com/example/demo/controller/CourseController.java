package com.example.demo.controller;

import com.example.demo.service.CourseSearcher;
import com.example.demo.model.CourseInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.demo.service.CourseService;

import java.util.List;

@RestController
@RequestMapping("/api/courses")


public class CourseController {
    @Autowired
    private CourseService courseService; 

    @Autowired
    private CourseSearcher courseSearcher;



    // Endpoint to get all courses
    @GetMapping("/all")
    public List<CourseInfo> getAllCourses() {
        return courseSearcher.getAllCourses();
    }

    // Endpoint to search courses by name using Trie


    // Endpoint to get ranked courses by rating (Ranking)
    @GetMapping("/ranked")
    public List<CourseInfo> getRankedCourses() {
        return courseSearcher.getRankedCourses();
    }

    // Endpoint to get sorted courses by duration (Sorting)
    @GetMapping("/sorted")
    public List<CourseInfo> getSortedCoursesByDuration() {
        return courseSearcher.getSortedCoursesByDuration();
    }

    // Endpoint to get courses that have prerequisites
    @GetMapping("/prerequisites")
    public List<CourseInfo> getCoursesWithPrerequisites() {
        return courseSearcher.getCoursesWithPrerequisites();
    }

    // Endpoint to add prerequisites for a course
    @PostMapping("/prerequisite")
    public String addPrerequisite(@RequestParam String courseId, @RequestParam String prerequisiteId) {
        courseSearcher.addPrerequisite(courseId, prerequisiteId);
        return "Prerequisite added!";
    }

    // Admin Endpoint to add a new course
    @PostMapping("/add")
    public String addCourse(@RequestBody CourseInfo course) {
        boolean success = courseSearcher.addCourse(course);
        if(success) {
            return "Course added successfully!";
        } else {
            return "Error adding course!";
        }
    }

    // Admin Endpoint to update an existing course
    @PutMapping("/update/{courseId}")
    public String updateCourse(@PathVariable String courseId, @RequestBody CourseInfo course) {
        boolean success = courseSearcher.updateCourse(courseId, course);
        if(success) {
            return "Course updated successfully!";
        } else {
            return "Error updating course!";
        }
    }

    // Admin Endpoint to delete a course
    @DeleteMapping("/delete/{courseId}")
    public String deleteCourse(@PathVariable String courseId) {
        boolean success = courseSearcher.deleteCourse(courseId);
        if(success) {
            return "Course deleted successfully!";
        } else {
            return "Error deleting course!";
        }
    }

    // Endpoint to get courses sorted by duration
    @GetMapping("/sortedByDuration")
    public List<CourseInfo> getCoursegetSortedCoursesByDuration() {
        return courseSearcher.getSortedCoursesByDuration();
    }

    @GetMapping("/search")
    public List<CourseInfo> search(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String minRating,
            @RequestParam(required = false) String maxRating
    		) {
        return courseService.searchAndFilterCourses(title, minRating, maxRating);
    }
}
