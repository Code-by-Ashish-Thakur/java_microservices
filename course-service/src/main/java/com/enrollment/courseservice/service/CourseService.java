package com.enrollment.courseservice.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.enrollment.courseservice.dto.CourseEvent;
import com.enrollment.courseservice.dto.CourseRequestDTO;
import com.enrollment.courseservice.entity.Course;
import com.enrollment.courseservice.exception.CourseNotFoundException;
import com.enrollment.courseservice.kafka.CourseEventProducer;
import com.enrollment.courseservice.repository.CourseRepository;

@Service
public class CourseService {

    private static final Logger log = LoggerFactory.getLogger(CourseService.class);

    private final CourseRepository courseRepository;
    private final CourseEventProducer eventProducer;

    public CourseService(CourseRepository courseRepository,
                         CourseEventProducer eventProducer) {
        this.courseRepository = courseRepository;
        this.eventProducer = eventProducer;
    }

    public Course createCourse(CourseRequestDTO requestDTO) {
        log.info("[COURSE-SERVICE] Creating course: {} | price: ${}", requestDTO.getName(), requestDTO.getPrice());
        Course course = new Course(
                requestDTO.getName(),
                requestDTO.getDescription(),
                requestDTO.getInstructor(),
                requestDTO.getDuration(),
                requestDTO.getMaxStudents(),
                requestDTO.getPrice()
        );
        Course savedCourse = courseRepository.save(course);
        log.info("[COURSE-SERVICE] Course saved to DB with id: {}", savedCourse.getId());

        CourseEvent event = new CourseEvent(
                "COURSE_CREATED",
                savedCourse.getId(),
                savedCourse.getName(),
                savedCourse.getInstructor(),
                savedCourse.getPrice()
        );
        eventProducer.publish(event);

        return savedCourse;
    }

    public List<Course> getAllCourses() {
        log.info("[COURSE-SERVICE] Fetching all courses from DB");
        return courseRepository.findAll();
    }

    public Course getCourseById(Long id) {
        log.info("[COURSE-SERVICE] Fetching course by id: {}", id);
        return courseRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("[COURSE-SERVICE] Course not found with id: {}", id);
                    return new CourseNotFoundException(id);
                });
    }

    public List<Course> searchCoursesByName(String name) {
        log.info("[COURSE-SERVICE] Searching courses by name: {}", name);
        return courseRepository.findByNameContainingIgnoreCase(name);
    }

    public Course updateCourse(Long id, CourseRequestDTO requestDTO) {
        log.info("[COURSE-SERVICE] Updating course id: {}", id);
        Course existing = courseRepository.findById(id)
                .orElseThrow(() -> new CourseNotFoundException(id));

        existing.setName(requestDTO.getName());
        existing.setDescription(requestDTO.getDescription());
        existing.setInstructor(requestDTO.getInstructor());
        existing.setDuration(requestDTO.getDuration());
        existing.setMaxStudents(requestDTO.getMaxStudents());
        existing.setPrice(requestDTO.getPrice());

        Course updated = courseRepository.save(existing);
        log.info("[COURSE-SERVICE] Course updated successfully: {}", updated.getName());

        CourseEvent event = new CourseEvent(
                "COURSE_UPDATED", updated.getId(), updated.getName(),
                updated.getInstructor(), updated.getPrice()
        );
        eventProducer.publish(event);

        return updated;
    }

    public void deleteCourse(Long id) {
        log.info("[COURSE-SERVICE] Deleting course id: {}", id);
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new CourseNotFoundException(id));
        courseRepository.deleteById(id);
        log.warn("[COURSE-SERVICE] Course deleted: {} (id={})", course.getName(), id);

        CourseEvent event = new CourseEvent(
                "COURSE_DELETED", course.getId(), course.getName(),
                course.getInstructor(), course.getPrice()
        );
        eventProducer.publish(event);
    }
}
