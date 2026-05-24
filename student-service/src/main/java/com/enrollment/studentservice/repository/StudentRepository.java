package com.enrollment.studentservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.enrollment.studentservice.entity.Student;

import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {

    Optional<Student> findByEmail(String email);

    boolean existsByEmail(String email);
}
