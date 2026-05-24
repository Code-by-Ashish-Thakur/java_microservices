package com.enrollment.courseservice.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.enrollment.courseservice.entity.Course;

public interface CourseRepository extends JpaRepository<Course, Long> {

    List<Course> findByNameContainingIgnoreCase(String name);
}
