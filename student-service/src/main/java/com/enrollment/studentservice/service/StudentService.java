package com.enrollment.studentservice.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.enrollment.studentservice.dto.StudentEvent;
import com.enrollment.studentservice.dto.StudentRequestDTO;
import com.enrollment.studentservice.entity.Student;
import com.enrollment.studentservice.exception.StudentNotFoundException;
import com.enrollment.studentservice.kafka.StudentEventProducer;
import com.enrollment.studentservice.repository.StudentRepository;

@Service
public class StudentService {

    private static final Logger log = LoggerFactory.getLogger(StudentService.class);

    private final StudentRepository studentRepository;
    private final StudentEventProducer eventProducer;

    public StudentService(StudentRepository studentRepository,
                          StudentEventProducer eventProducer) {
        this.studentRepository = studentRepository;
        this.eventProducer = eventProducer;
    }

    public Student createStudent(StudentRequestDTO requestDTO) {
        log.info("[STUDENT-SERVICE] Creating student with email: {}", requestDTO.getEmail());

        if (studentRepository.existsByEmail(requestDTO.getEmail())) {
            log.warn("[STUDENT-SERVICE] Duplicate email detected: {}", requestDTO.getEmail());
            throw new RuntimeException("Student with email " + requestDTO.getEmail() + " already exists");
        }

        Student student = new Student(
                requestDTO.getName(),
                requestDTO.getEmail(),
                requestDTO.getPhone()
        );
        Student savedStudent = studentRepository.save(student);
        log.info("[STUDENT-SERVICE] Student saved to DB with id: {}", savedStudent.getId());

        // Publish Kafka event (fire-and-forget, async)
        StudentEvent event = new StudentEvent(
                "STUDENT_CREATED",
                savedStudent.getId(),
                savedStudent.getName(),
                savedStudent.getEmail(),
                savedStudent.getPhone()
        );
        eventProducer.publishStudentCreated(event);

        return savedStudent;
    }

    public List<Student> getAllStudents() {
        log.info("[STUDENT-SERVICE] Fetching all students from DB");
        return studentRepository.findAll();
    }

    public Student getStudentById(Long id) {
        log.info("[STUDENT-SERVICE] Fetching student by id: {}", id);
        return studentRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("[STUDENT-SERVICE] Student not found with id: {}", id);
                    return new StudentNotFoundException(id);
                });
    }
}
