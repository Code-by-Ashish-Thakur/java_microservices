package com.enrollment.enrollmentservice.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.enrollment.enrollmentservice.dto.EnrollmentRequestDTO;
import com.enrollment.enrollmentservice.dto.EnrollmentResponseDTO;
import com.enrollment.enrollmentservice.service.EnrollmentService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/enrollments")
public class EnrollmentController {

    private static final Logger log = LoggerFactory.getLogger(EnrollmentController.class);

    private final EnrollmentService enrollmentService;

    public EnrollmentController(EnrollmentService enrollmentService) {
        this.enrollmentService = enrollmentService;
    }

    // POST /api/enrollments - enroll student (triggers full saga)
    @PostMapping
    public ResponseEntity<EnrollmentResponseDTO> createEnrollment(
            @Valid @RequestBody EnrollmentRequestDTO requestDTO) {
        log.info("[ENROLLMENT-CONTROLLER] POST /api/enrollments - Creating enrollment for studentId={}, courseId={}",
                requestDTO.getStudentId(), requestDTO.getCourseId());
        EnrollmentResponseDTO response = enrollmentService.createEnrollment(requestDTO);
        log.info("[ENROLLMENT-CONTROLLER] Enrollment created successfully with id={}, status={}",
                response.getEnrollmentId(), response.getStatus());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // GET /api/enrollments - list all with enriched data
    @GetMapping
    public List<EnrollmentResponseDTO> getAllEnrollments() {
        log.info("[ENROLLMENT-CONTROLLER] GET /api/enrollments - Fetching all enrollments");
        List<EnrollmentResponseDTO> enrollments = enrollmentService.getAllEnrollments();
        log.info("[ENROLLMENT-CONTROLLER] Returning {} enrollment(s)", enrollments.size());
        return enrollments;
    }

    // GET /api/enrollments/{id} - single enrollment
    @GetMapping("/{id}")
    public EnrollmentResponseDTO getEnrollmentById(@PathVariable Long id) {
        log.info("[ENROLLMENT-CONTROLLER] GET /api/enrollments/{} - Fetching enrollment by id", id);
        EnrollmentResponseDTO response = enrollmentService.getEnrollmentById(id);
        log.info("[ENROLLMENT-CONTROLLER] Found enrollment id={}, status={}", response.getEnrollmentId(), response.getStatus());
        return response;
    }

    // GET /api/enrollments/student/{studentId} - all enrollments for a student
    @GetMapping("/student/{studentId}")
    public List<EnrollmentResponseDTO> getEnrollmentsByStudent(@PathVariable Long studentId) {
        log.info("[ENROLLMENT-CONTROLLER] GET /api/enrollments/student/{} - Fetching enrollments by studentId", studentId);
        List<EnrollmentResponseDTO> enrollments = enrollmentService.getEnrollmentsByStudent(studentId);
        log.info("[ENROLLMENT-CONTROLLER] Returning {} enrollment(s) for studentId={}", enrollments.size(), studentId);
        return enrollments;
    }
}
