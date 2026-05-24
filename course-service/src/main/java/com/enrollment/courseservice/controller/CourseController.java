package com.enrollment.courseservice.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.enrollment.courseservice.dto.CourseRequestDTO;
import com.enrollment.courseservice.entity.Course;
import com.enrollment.courseservice.service.CourseService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/courses")
public class CourseController {

    private static final Logger log = LoggerFactory.getLogger(CourseController.class);

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @PostMapping
    public ResponseEntity<Course> createCourse(@Valid @RequestBody CourseRequestDTO requestDTO) {
        log.info("[COURSE-CONTROLLER] POST /api/courses - Creating course: {}", requestDTO.getName());
        Course created = courseService.createCourse(requestDTO);
        log.info("[COURSE-CONTROLLER] Course created with id: {}", created.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    public List<Course> getAllCourses() {
        log.info("[COURSE-CONTROLLER] GET /api/courses - Fetching all courses");
        return courseService.getAllCourses();
    }

    @GetMapping("/{id}")
    public Course getCourseById(@PathVariable Long id) {
        log.info("[COURSE-CONTROLLER] GET /api/courses/{} - Fetching course", id);
        return courseService.getCourseById(id);
    }

    @GetMapping("/search")
    public List<Course> searchCourses(@RequestParam String name) {
        log.info("[COURSE-CONTROLLER] GET /api/courses/search?name={}", name);
        return courseService.searchCoursesByName(name);
    }

    @PutMapping("/{id}")
    public Course updateCourse(@PathVariable Long id,
                               @Valid @RequestBody CourseRequestDTO requestDTO) {
        log.info("[COURSE-CONTROLLER] PUT /api/courses/{} - Updating course", id);
        return courseService.updateCourse(id, requestDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable Long id) {
        log.info("[COURSE-CONTROLLER] DELETE /api/courses/{} - Deleting course", id);
        courseService.deleteCourse(id);
        return ResponseEntity.noContent().build();
    }
}
