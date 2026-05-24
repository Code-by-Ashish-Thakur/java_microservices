package com.enrollment.enrollmentservice.dto;

import java.time.LocalDateTime;

// ============================================================
// ENROLLMENT RESPONSE DTO - Aggregated data from 3 services
// ============================================================
// Combines data from:
//   1. Enrollment Service (enrollmentId, status, enrolledAt)
//   2. Student Service    (studentName, studentEmail)
//   3. Course Service     (courseName, instructor, price)
//
// This is the API Composition / Aggregator Pattern.
// ============================================================

public class EnrollmentResponseDTO {

    private Long enrollmentId;
    private String status;
    private LocalDateTime enrolledAt;

    // From Student Service
    private Long studentId;
    private String studentName;
    private String studentEmail;

    // From Course Service
    private Long courseId;
    private String courseName;
    private String instructor;
    private double coursePrice;

    public EnrollmentResponseDTO() {}

    public EnrollmentResponseDTO(Long enrollmentId, String status, LocalDateTime enrolledAt,
                                  Long studentId, String studentName, String studentEmail,
                                  Long courseId, String courseName, String instructor,
                                  double coursePrice) {
        this.enrollmentId = enrollmentId;
        this.status = status;
        this.enrolledAt = enrolledAt;
        this.studentId = studentId;
        this.studentName = studentName;
        this.studentEmail = studentEmail;
        this.courseId = courseId;
        this.courseName = courseName;
        this.instructor = instructor;
        this.coursePrice = coursePrice;
    }

    // Getters and Setters
    public Long getEnrollmentId() { return enrollmentId; }
    public void setEnrollmentId(Long enrollmentId) { this.enrollmentId = enrollmentId; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getEnrolledAt() { return enrolledAt; }
    public void setEnrolledAt(LocalDateTime enrolledAt) { this.enrolledAt = enrolledAt; }

    public Long getStudentId() { return studentId; }
    public void setStudentId(Long studentId) { this.studentId = studentId; }

    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }

    public String getStudentEmail() { return studentEmail; }
    public void setStudentEmail(String studentEmail) { this.studentEmail = studentEmail; }

    public Long getCourseId() { return courseId; }
    public void setCourseId(Long courseId) { this.courseId = courseId; }

    public String getCourseName() { return courseName; }
    public void setCourseName(String courseName) { this.courseName = courseName; }

    public String getInstructor() { return instructor; }
    public void setInstructor(String instructor) { this.instructor = instructor; }

    public double getCoursePrice() { return coursePrice; }
    public void setCoursePrice(double coursePrice) { this.coursePrice = coursePrice; }
}
