package com.enrollment.enrollmentservice.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

// ============================================================
// ENROLLMENT ENTITY - Now with STATUS for Saga Pattern
// ============================================================
// The enrollment lifecycle:
//   1. PENDING   → Just created, waiting for payment
//   2. CONFIRMED → Payment succeeded
//   3. FAILED    → Payment failed
//
// This demonstrates the Saga Pattern:
//   - Enrollment Service creates enrollment (PENDING)
//   - Payment Service processes payment asynchronously
//   - Payment event arrives via Kafka → status updated
// ============================================================

@Entity
@Table(name = "enrollments")
public class Enrollment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long studentId;
    private Long courseId;

    private String status;           // PENDING, CONFIRMED, FAILED
    private LocalDateTime enrolledAt;
    private LocalDateTime updatedAt;

    public Enrollment() {}

    public Enrollment(Long studentId, Long courseId) {
        this.studentId = studentId;
        this.courseId = courseId;
        this.status = "PENDING";
        this.enrolledAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getStudentId() { return studentId; }
    public void setStudentId(Long studentId) { this.studentId = studentId; }

    public Long getCourseId() { return courseId; }
    public void setCourseId(Long courseId) { this.courseId = courseId; }

    public String getStatus() { return status; }
    public void setStatus(String status) {
        this.status = status;
        this.updatedAt = LocalDateTime.now();
    }

    public LocalDateTime getEnrolledAt() { return enrolledAt; }
    public void setEnrolledAt(LocalDateTime enrolledAt) { this.enrolledAt = enrolledAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
