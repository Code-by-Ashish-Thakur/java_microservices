package com.enrollment.enrollmentservice.service;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import com.enrollment.enrollmentservice.dto.*;
import com.enrollment.enrollmentservice.entity.Enrollment;
import com.enrollment.enrollmentservice.exception.EnrollmentNotFoundException;
import com.enrollment.enrollmentservice.kafka.EnrollmentEventProducer;
import com.enrollment.enrollmentservice.repository.EnrollmentRepository;

// ============================================================
// ENROLLMENT SERVICE - Orchestrates the enrollment flow
// ============================================================
// FLOW:
//   1. Receive enrollment request (studentId + courseId)
//   2. Call Student Service via RestTemplate (sync) to verify
//   3. Call Course Service via RestTemplate (sync) to verify
//   4. Save enrollment with status PENDING
//   5. Publish ENROLLMENT_CREATED event to Kafka (async)
//   6. Payment service picks up event → processes payment
//   7. Payment event arrives → PaymentEventConsumer updates status
// ============================================================

@Service
public class EnrollmentService {

    private static final Logger log = LoggerFactory.getLogger(EnrollmentService.class);

    private static final String STUDENT_SERVICE_URL = "http://localhost:8081/api/students";
    private static final String COURSE_SERVICE_URL = "http://localhost:8083/api/courses";

    private final EnrollmentRepository enrollmentRepository;
    private final RestTemplate restTemplate;
    private final EnrollmentEventProducer eventProducer;

    public EnrollmentService(EnrollmentRepository enrollmentRepository,
                             RestTemplate restTemplate,
                             EnrollmentEventProducer eventProducer) {
        this.enrollmentRepository = enrollmentRepository;
        this.restTemplate = restTemplate;
        this.eventProducer = eventProducer;
    }

    public EnrollmentResponseDTO createEnrollment(EnrollmentRequestDTO requestDTO) {
        log.info("[ENROLLMENT-SERVICE] Creating enrollment for studentId={}, courseId={}",
                requestDTO.getStudentId(), requestDTO.getCourseId());

        // Step 1: Verify student exists (sync REST call)
        log.info("[ENROLLMENT-SERVICE] Step 1: Fetching student with id={}", requestDTO.getStudentId());
        StudentDTO student = fetchStudent(requestDTO.getStudentId());

        // Step 2: Verify course exists (sync REST call)
        log.info("[ENROLLMENT-SERVICE] Step 2: Fetching course with id={}", requestDTO.getCourseId());
        CourseDTO course = fetchCourse(requestDTO.getCourseId());

        // Step 3: Save enrollment with PENDING status
        log.info("[ENROLLMENT-SERVICE] Step 3: Saving enrollment with PENDING status");
        Enrollment enrollment = new Enrollment(requestDTO.getStudentId(), requestDTO.getCourseId());
        Enrollment saved = enrollmentRepository.save(enrollment);
        log.info("[ENROLLMENT-SERVICE] Enrollment saved with id={}, status={}", saved.getId(), saved.getStatus());

        // Step 4: Publish to Kafka (async) - payment-service will pick this up
        log.info("[ENROLLMENT-SERVICE] Step 4: Publishing ENROLLMENT_CREATED event to Kafka");
        EnrollmentEvent event = new EnrollmentEvent(
                "ENROLLMENT_CREATED",
                saved.getId(),
                student.getId(), student.getName(), student.getEmail(),
                course.getId(), course.getName(), course.getPrice()
        );
        eventProducer.publishEnrollmentCreated(event);

        // Step 5: Return aggregated response
        log.info("[ENROLLMENT-SERVICE] Step 5: Enrollment flow complete, returning response");
        return buildResponse(saved, student, course);
    }

    public List<EnrollmentResponseDTO> getAllEnrollments() {
        log.info("[ENROLLMENT-SERVICE] Fetching all enrollments");
        List<EnrollmentResponseDTO> results = enrollmentRepository.findAll().stream()
                .map(this::enrichEnrollment)
                .collect(Collectors.toList());
        log.info("[ENROLLMENT-SERVICE] Found {} enrollment(s)", results.size());
        return results;
    }

    public EnrollmentResponseDTO getEnrollmentById(Long id) {
        log.info("[ENROLLMENT-SERVICE] Fetching enrollment by id={}", id);
        Enrollment enrollment = enrollmentRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("[ENROLLMENT-SERVICE] Enrollment not found with id={}", id);
                    return new EnrollmentNotFoundException(id);
                });
        return enrichEnrollment(enrollment);
    }

    public List<EnrollmentResponseDTO> getEnrollmentsByStudent(Long studentId) {
        log.info("[ENROLLMENT-SERVICE] Fetching enrollments for studentId={}", studentId);
        List<EnrollmentResponseDTO> results = enrollmentRepository.findByStudentId(studentId).stream()
                .map(this::enrichEnrollment)
                .collect(Collectors.toList());
        log.info("[ENROLLMENT-SERVICE] Found {} enrollment(s) for studentId={}", results.size(), studentId);
        return results;
    }

    // ==================== PRIVATE HELPERS ====================

    private StudentDTO fetchStudent(Long studentId) {
        try {
            String url = STUDENT_SERVICE_URL + "/" + studentId;
            log.info("[ENROLLMENT-SERVICE] >>> REST call: GET {}", url);
            StudentDTO student = restTemplate.getForObject(url, StudentDTO.class);
            if (student == null) {
                log.error("[ENROLLMENT-ERROR] Student Service returned null for studentId={}", studentId);
                throw new RuntimeException("Student not found with id: " + studentId);
            }
            log.info("[ENROLLMENT-SERVICE] <<< Student fetched: id={}, name={}", student.getId(), student.getName());
            return student;
        } catch (HttpClientErrorException e) {
            log.error("[ENROLLMENT-ERROR] Student Service returned {} for studentId={}", e.getStatusCode(), studentId);
            throw new RuntimeException("Student not found with id: " + studentId +
                    " (Student Service returned " + e.getStatusCode() + ")");
        } catch (ResourceAccessException e) {
            log.error("[ENROLLMENT-ERROR] Student Service is unavailable at {}", STUDENT_SERVICE_URL);
            throw new RuntimeException("Student Service is unavailable! Make sure it's running on port 8081.");
        }
    }

    private CourseDTO fetchCourse(Long courseId) {
        try {
            String url = COURSE_SERVICE_URL + "/" + courseId;
            log.info("[ENROLLMENT-SERVICE] >>> REST call: GET {}", url);
            CourseDTO course = restTemplate.getForObject(url, CourseDTO.class);
            if (course == null) {
                log.error("[ENROLLMENT-ERROR] Course Service returned null for courseId={}", courseId);
                throw new RuntimeException("Course not found with id: " + courseId);
            }
            log.info("[ENROLLMENT-SERVICE] <<< Course fetched: id={}, name={}", course.getId(), course.getName());
            return course;
        } catch (HttpClientErrorException e) {
            log.error("[ENROLLMENT-ERROR] Course Service returned {} for courseId={}", e.getStatusCode(), courseId);
            throw new RuntimeException("Course not found with id: " + courseId +
                    " (Course Service returned " + e.getStatusCode() + ")");
        } catch (ResourceAccessException e) {
            log.error("[ENROLLMENT-ERROR] Course Service is unavailable at {}", COURSE_SERVICE_URL);
            throw new RuntimeException("Course Service is unavailable! Make sure it's running on port 8083.");
        }
    }

    private EnrollmentResponseDTO enrichEnrollment(Enrollment enrollment) {
        try {
            StudentDTO student = fetchStudent(enrollment.getStudentId());
            CourseDTO course = fetchCourse(enrollment.getCourseId());
            return buildResponse(enrollment, student, course);
        } catch (RuntimeException e) {
            // If dependent services are down, return partial data
            log.warn("[ENROLLMENT-SERVICE] Dependent service unavailable, returning partial data for enrollmentId={}: {}",
                    enrollment.getId(), e.getMessage());
            EnrollmentResponseDTO response = new EnrollmentResponseDTO();
            response.setEnrollmentId(enrollment.getId());
            response.setStatus(enrollment.getStatus());
            response.setEnrolledAt(enrollment.getEnrolledAt());
            response.setStudentId(enrollment.getStudentId());
            response.setStudentName("(unavailable)");
            response.setStudentEmail("(unavailable)");
            response.setCourseId(enrollment.getCourseId());
            response.setCourseName("(unavailable)");
            response.setInstructor("(unavailable)");
            return response;
        }
    }

    private EnrollmentResponseDTO buildResponse(Enrollment enrollment, StudentDTO student, CourseDTO course) {
        return new EnrollmentResponseDTO(
                enrollment.getId(), enrollment.getStatus(), enrollment.getEnrolledAt(),
                student.getId(), student.getName(), student.getEmail(),
                course.getId(), course.getName(), course.getInstructor(), course.getPrice()
        );
    }
}
