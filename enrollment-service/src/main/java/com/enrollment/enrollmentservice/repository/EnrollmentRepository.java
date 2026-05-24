package com.enrollment.enrollmentservice.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.enrollment.enrollmentservice.entity.Enrollment;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {

    List<Enrollment> findByStudentId(Long studentId);

    List<Enrollment> findByCourseId(Long courseId);

    List<Enrollment> findByStatus(String status);
}
